package com.example.weatherapp.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.model.Forecast

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
    private lateinit var currentDegreeTV:TextView
    private lateinit var currentDescriptionTV:TextView
    private lateinit var windSpeedTV:TextView
    private lateinit var rainChanceTV:TextView
    private lateinit var pressureTV:TextView
    private lateinit var humidityTV:TextView
    private lateinit var viewModel:CurrentForecastViewModel
    private fun initializeComponents(view: View)
    {
        currentDegreeTV=view.findViewById(R.id.degree_text)
        currentDescriptionTV=view.findViewById(R.id.weather_text)
        windSpeedTV=view.findViewById(R.id.wind_speed_text)
        rainChanceTV=view.findViewById(R.id.rain_chance_value_text)
        pressureTV=view.findViewById(R.id.pressure_value_text)
        humidityTV=view.findViewById(R.id.humidity_value_text)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents(view)

        viewModel= ViewModelProvider(this)[CurrentForecastViewModel::class.java]
        viewModel.getCurrentForecast(31.124344,29.780860,API_KEY)

        viewModel.forecast.observe(requireActivity()){
                Log.i("TAG", "onViewCreated: $it")
                setUiComponents(it)
            }
        viewModel.getFiveForecast(31.124344,29.780860,API_KEY)
        viewModel.fiveForecast.observe(requireActivity()) {
            Log.i("TAG", "onViewCreated2:$it ")
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
    }
}
