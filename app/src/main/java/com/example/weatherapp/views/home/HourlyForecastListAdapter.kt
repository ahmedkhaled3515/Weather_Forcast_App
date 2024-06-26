package com.example.weatherapp.views.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.SettingsSharedPreferences
import com.example.weatherapp.model.DailyWeather
import com.example.weatherapp.model.HourlyWeather
import java.text.NumberFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HourlyForecastListAdapter(val context: Context) : ListAdapter<HourlyWeather, HourlyForecastListAdapter.ViewHolder>(HourlyWeatherDiffUtil()) {
    private lateinit var settingSharedPreferences: SettingsSharedPreferences
    private lateinit var units : String
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTV:TextView=view.findViewById(R.id.time_value_text)
        val timeIcon:ImageView=view.findViewById(R.id.time_icon)
        val hourlyDegree:TextView=view.findViewById(R.id.hourly_degree_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.hourly_card_layout,parent,false)
        settingSharedPreferences = SettingsSharedPreferences
        units = settingSharedPreferences.getUnits(context)!!
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecast=getItem(position)
        setCardContent(holder,forecast)
    }
    @SuppressLint("SetTextI18n")
    private fun setCardContent(holder: ViewHolder, forecast: HourlyWeather)
    {
        var date = timestampToDate(forecast.dt)
        val calendar=Calendar.getInstance()
        calendar.time=date
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getInstance(locale)
        val period : String= if(calendar.get(Calendar.AM_PM) == 0)
        {
            context.getString(R.string.AM)
        }
        else{
            context.getString(R.string.PM)
        }

        if (Calendar.getInstance().get(Calendar.DATE) == calendar.get(Calendar.DATE)){
            Log.i("TAG", "setCardContent: true ")
        }else
        {
            Log.i("TAG", "setCardContent: false ")

        }
        Log.i("TAG", "onBindViewHolder: ${forecast.dt}")
        if (units == "metric")
        {
            holder.hourlyDegree.text= "${numberFormat.format(forecast.temp)}°C"
        }
        else if (units == "imperial")
        {
            holder.hourlyDegree.text= "${numberFormat.format(forecast.temp)}°F"
        }
        else{
            holder.hourlyDegree.text= "${numberFormat.format(forecast.temp)}°K"
        }

        holder.timeTV.text= if(calendar.get(Calendar.HOUR) == 0)
        {
           "${numberFormat.format(12)} $period"
        }
        else{
             "${numberFormat.format(calendar.get(Calendar.HOUR))} $period"
        }
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
            .into(holder.timeIcon)
//        ${calendar.get(Calendar.HOUR)} $period
    }
}

//fun timestampToDate(timestamp: Long): Date {
//    return Date(timestamp)
//}
fun timestampToDate(timestamp: Long): Date {
    // Convert seconds to milliseconds
    val milliseconds = timestamp * 1000
    return Date(milliseconds)
}
@RequiresApi(Build.VERSION_CODES.O)
fun hourConverter(time:String) {
//    val time24Hour = "13:30" // Example 24-hour format time

    // Parse the 24-hour time string into a LocalTime object
    val time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))

    // Format the LocalTime object into a 12-hour format with AM/PM indication
    val formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm a"))

    Log.i("TAG", "Converted time: $formattedTime") // Output: Converted time: 01:30 PM
}

class DailyWeatherDiffUtil : DiffUtil.ItemCallback<DailyWeather>() {
    override fun areItemsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean {
        return oldItem == newItem
    }

}class HourlyWeatherDiffUtil : DiffUtil.ItemCallback<HourlyWeather>() {
    override fun areItemsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
        return oldItem == newItem
    }

}
