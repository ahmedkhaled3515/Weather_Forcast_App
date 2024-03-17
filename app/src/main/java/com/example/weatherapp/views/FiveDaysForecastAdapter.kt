package com.example.weatherapp.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.Forecast
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FiveDaysForecastAdapter(val context: Context) : ListAdapter<Forecast,FiveDaysForecastAdapter.ViewHolder>(ForecastDiffUtil()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val dayText:TextView = view.findViewById(R.id.day_name_text)
        val dayImage:ImageView = view.findViewById(R.id.day_image)
        val dayDesc:TextView = view.findViewById(R.id.day_desc_text)
        val dayMinMaxTemp:TextView = view.findViewById(R.id.day_max_min_text)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FiveDaysForecastAdapter.ViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.days_card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FiveDaysForecastAdapter.ViewHolder, position: Int) {
        val forecast= getItem(position)
        val calendar=Calendar.getInstance()
        calendar.timeInMillis= forecast.dt*1000
        holder.dayText.text= getDayName(calendar).substring(0,3).uppercase()
        holder.dayDesc.text= forecast.weather[0].description
        holder.dayMinMaxTemp.text= "${forecast.main.tempMax}/${forecast.main.tempMin}°C"
        Glide.with(context)
            .load("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
            .into(holder.dayImage)
    }
    fun getDayName(date: Calendar): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(date.time)
    }
}