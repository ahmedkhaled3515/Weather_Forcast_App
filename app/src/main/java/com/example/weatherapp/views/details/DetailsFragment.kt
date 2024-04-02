package com.example.weatherapp.views.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.ApiState
import com.example.weatherapp.R
import com.example.weatherapp.SettingsSharedPreferences
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.ViewModel.CurrentForecastViewModelFactory
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.WeatherSharedPreferences
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import com.example.weatherapp.views.home.FiveDaysForecastAdapter
import com.example.weatherapp.views.home.HourlyForecastListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    companion object{
        const val API_KEY="172e0cbb3264b27530f5b6c425ffb29d"
    }
    //    private var language = "en"
    private var language : String? = "en"
    private var units : String? = "metric"
    private lateinit var settingsSharedPreferences: SettingsSharedPreferences
    private lateinit var layout: ConstraintLayout
    var currentLat:Double?=null
    var currentLon:Double?=null
    private lateinit var weatherIcon: ImageView
    private lateinit var currentDegreeTV: TextView
    private lateinit var currentDescriptionTV: TextView
    private lateinit var windSpeedTV: TextView
    private lateinit var rainChanceTV: TextView
    private lateinit var pressureTV: TextView
    private lateinit var humidityTV: TextView
    private lateinit var viewModel: CurrentForecastViewModel
    private lateinit var hourlyRV: RecyclerView
    private lateinit var dailyRV: RecyclerView
    private lateinit var locationText: TextView
    private var currentSuccess:Boolean?=false
    private lateinit var progress: ProgressBar
    private val sharedSettingsViewModel : SharedSettingsViewModel by activityViewModels()
    private lateinit var weatherSharedPreferences : WeatherSharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsSharedPreferences = SettingsSharedPreferences
        language = settingsSharedPreferences.getLanguage(requireContext())
        units = settingsSharedPreferences.getUnits(requireContext())
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this, CurrentForecastViewModelFactory(
            AppRepository.getInstance(
                RemoteDataSource(), LocalDataSource(
            AppDatabase.getInstance(requireContext()))
            ))
        ).get(CurrentForecastViewModel::class.java)
        val bundle = arguments
        if(bundle != null)
        {
            val type = bundle.getString("type")
            currentLat = bundle.getDouble("lat")
            currentLon = bundle.getDouble("long")
            viewModel.getForecastResponse(currentLat!!,currentLon!!, API_KEY, units = units!!,
                language!!
            )
        }
        weatherSharedPreferences = WeatherSharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


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
        locationText=view.findViewById(R.id.location_textview)
        dailyRV=view.findViewById(R.id.days_RV)
        layout=view.findViewById(R.id.frameLayout)
        progress=view.findViewById(R.id.progress_bar)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initializeComponents(view)
//        viewModel= ViewModelProvider(this)[CurrentForecastViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.forecast.collect(){
                    when(it)
                    {
                        is ApiState.Success ->{
                            weatherSharedPreferences.saveWeatherResponse(requireContext(),it.data)
                            setUiComponents(it.data)
                            currentSuccess=true
                            progress.visibility=View.GONE

                        }
                        is ApiState.Failure ->{
                            if(weatherSharedPreferences.getWeatherResponse(requireContext()) != null)
                            {
                                weatherSharedPreferences.getWeatherResponse(requireContext())
                                    ?.let { it1 -> setUiComponents(it1) }
                                Toast.makeText(requireActivity(),"No Internet Offline Mode", Toast.LENGTH_SHORT).show()
                                progress.visibility = View.GONE
                            }
                            else{
                                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                                it.msg.printStackTrace()
                            }

                        }
                        else -> {
                            Toast.makeText(requireActivity(),"Loading", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            sharedSettingsViewModel.language.collect(){
                repeatOnLifecycle(Lifecycle.State.STARTED)
                {
                    language = it
//                    changeLanguage(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            sharedSettingsViewModel.unitsFlow.collect(){
                repeatOnLifecycle(Lifecycle.State.STARTED)
                {
                    units = it
//                    changeLanguage(it)
                }
            }
        }

    }

    private fun isSameDay(timestamp: Long) : Boolean{
        val calendar= Calendar.getInstance()
        calendar.timeInMillis=timestamp*1000
        Log.i("TAG", "isSameDay: ${calendar.time}")
        return Calendar.getInstance().get(Calendar.DATE) == calendar.get(Calendar.DATE)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    private fun setUiComponents(weatherResponse: WeatherResponse)
    {
        val forecast = weatherResponse.current
        Log.i("TAG", "setUiComponents: ${sharedSettingsViewModel.language}")
        currentDegreeTV.text="${forecast.temp}°c"
        currentDescriptionTV.text=forecast.weather[0].description
        windSpeedTV.text=forecast.windSpeed.toString()
        rainChanceTV.text= forecast.rain?.toString()?:"No chance"
        pressureTV.text=forecast.pressure.toString()
        humidityTV.text=forecast.humidity.toString()
        locationText.text=weatherResponse.timezone
//        getAddressWithCoordinates(forecast.coord.lat.toDouble(),forecast.coord.lon.toDouble())
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
            .into(weatherIcon)
        dailyRV.apply {
            layoutManager= LinearLayoutManager(context).apply {
                orientation= LinearLayoutManager.VERTICAL
            }
            adapter = FiveDaysForecastAdapter(context).apply {
                submitList(weatherResponse.daily)
            }
        }
        hourlyRV.apply {
            layoutManager= LinearLayoutManager(context).apply {
                orientation= LinearLayoutManager.HORIZONTAL
            }
            adapter = HourlyForecastListAdapter(context).apply {
                submitList(weatherResponse.hourly.take(24))
            }
        }
    }
}