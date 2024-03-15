package com.example.weatherapp.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.model.Forecast
import java.util.Calendar
import java.util.Date
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    val API_KEY="172e0cbb3264b27530f5b6c425ffb29d"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

    }
    private lateinit var weatherIcon:ImageView
    private lateinit var currentDegreeTV:TextView
    private lateinit var currentDescriptionTV:TextView
    private lateinit var windSpeedTV:TextView
    private lateinit var rainChanceTV:TextView
    private lateinit var pressureTV:TextView
    private lateinit var humidityTV:TextView
    private lateinit var viewModel:CurrentForecastViewModel
    private lateinit var hourlyRV:RecyclerView
    private fun initializeComponents(view: View)
    {
        currentDegreeTV=view.findViewById(R.id.degree_text)
        currentDescriptionTV=view.findViewById(R.id.weather_text)
        windSpeedTV=view.findViewById(R.id.wind_speed_text)
        rainChanceTV=view.findViewById(R.id.rain_chance_value_text)
        pressureTV=view.findViewById(R.id.pressure_value_text)
        humidityTV=view.findViewById(R.id.humidity_value_text)
        hourlyRV=view.findViewById(R.id.hour_recycler_view)
        weatherIcon=view.findViewById(R.id.weather_image)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents(view)

        viewModel= ViewModelProvider(this)[CurrentForecastViewModel::class.java]
        viewModel.getCurrentForecast(31.124344,29.780860,API_KEY)

        viewModel.forecast.observe(requireActivity()){
            Log.i("TAG", "onViewCreated3333: ${Calendar.getInstance().time}")
                Log.i("TAG", "onViewCreated: $it")
                setUiComponents(it)
            }
        viewModel.getFiveForecast(31.124344,29.780860,API_KEY)
        viewModel.fiveForecast.observe(requireActivity()) {
//            val list= mutableListOf<Forecast>()
//            for(forecast in it.list )
//            {
//                val calendar=Calendar.getInstance()
//                calendar.time=timestampToDate(forecast.dt)
//                if(calendar.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE))
//                {
//                    list.add(forecast)
//                }
//            }

            Log.i("TAG", "onViewCreated2:${it.list} ")
            hourlyRV.apply {
                layoutManager=LinearLayoutManager(requireContext()).apply {
                    orientation=LinearLayoutManager.HORIZONTAL
                }
                adapter = HourlyForecastListAdapter(requireContext()).apply {
                    submitList(it.list)
                }
            }

        }

    }
    @SuppressLint("SetTextI18n")
    private fun setUiComponents(forecast: Forecast)
    {
        currentDegreeTV.text="${forecast.main.temp}°c"
        currentDescriptionTV.text=forecast.weather[0].description
        windSpeedTV.text=forecast.wind.speed.toString()
        rainChanceTV.text= forecast.rain?.toString()?:"No chance"
        pressureTV.text=forecast.main.pressure.toString()
        humidityTV.text=forecast.main.humidity.toString()
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
            .into(weatherIcon)
    }
    fun timestampToDate(timestamp: Long): Date {
        // Convert seconds to milliseconds
        val milliseconds = timestamp * 1000
        return Date(milliseconds)
    }
}
