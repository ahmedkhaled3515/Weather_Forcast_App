package com.example.weatherapp.views

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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.ApiState
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.model.WeatherResponse
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
class HomeFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    companion object{
          const val API_KEY="172e0cbb3264b27530f5b6c425ffb29d"
    }
    private lateinit var layout: ConstraintLayout

    var currentLat:Double?=null
    var currentLon:Double?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        getLocation()
        getLocation()
        sharedPreferences = requireActivity().getSharedPreferences("myShared",Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    private lateinit var weatherIcon:ImageView
    private lateinit var currentDegreeTV:TextView
    private lateinit var currentDescriptionTV:TextView
    private lateinit var windSpeedTV:TextView
    private lateinit var rainChanceTV:TextView
    private lateinit var pressureTV:TextView
    private lateinit var humidityTV:TextView
    private val viewModel:CurrentForecastViewModel by activityViewModels()
    private lateinit var hourlyRV:RecyclerView
    private lateinit var dailyRV:RecyclerView
    private lateinit var locationText:TextView
    private var currentSuccess:Boolean?=false
    private var fiveSuccess:Boolean?=false
    private lateinit var progress:ProgressBar

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
        val localDataSource = LocalDataSource(requireActivity())
//        viewModel= ViewModelProvider(this)[CurrentForecastViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.forecast.collect(){
                    when(it)
                    {
                        is ApiState.Success ->{
//                            isSameDay(it.data)
                            setUiComponents(it.data)
                            currentSuccess=true
                            progress.visibility=View.GONE

                        }
                        is ApiState.Failure ->{
                            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                            it.msg.printStackTrace()
                        }
                        else -> {
                            Toast.makeText(requireActivity(),"Loading",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED)
//            {
//                viewModel.fiveForecast
//                    .collect()
//                    {
//                        when(it){
//                            is ApiState.Success -> {
//                                val newList= it.data.list.take(8)
//                                Log.i("TAG", "onViewCreated2:${it} ")
//                                hourlyRV.apply {
//                                    layoutManager=LinearLayoutManager(requireContext()).apply {
//                                        orientation=LinearLayoutManager.HORIZONTAL
//                                    }
//                                    adapter = HourlyForecastListAdapter(requireContext()).apply {
//                                        submitList(newList)
//                                    }
//                                    fiveSuccess=true
//                                    if(currentSuccess == true)
//                                    {
//                                        layout.visibility=View.VISIBLE
//                                        progress.visibility=View.GONE
//
//                                    }
//                                }
//                            }
//                            is ApiState.Failure ->{
//                                Toast.makeText(requireActivity(),"Failed",Toast.LENGTH_SHORT).show()
//                                it.msg.printStackTrace()
//                            }
//                            else -> {
//                                Toast.makeText(requireActivity(),"Loading",Toast.LENGTH_SHORT).show()
//                                layout.visibility= View.GONE
//                                progress.visibility=View.VISIBLE
//
//                            }
//                        }
//                    }
//            }
//
//        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED){
//                viewModel.fiveForecast
//                    .collect()
//                    {
//                        when(it){
//                            is ApiState.Success -> {
//                                val newList= it.data.list.distinctBy {
//                                        forecast ->
//                                    val calendar=Calendar.getInstance()
//                                    calendar.timeInMillis= forecast.dt*1000
//                                    calendar.get(Calendar.DATE)
//                                }
//                                dailyRV.apply {
//                                    layoutManager=LinearLayoutManager(this.context).apply {
//                                        orientation=LinearLayoutManager.VERTICAL
//                                    }
//                                    adapter=FiveDaysForecastAdapter(this.context).apply {
//                                        submitList(newList)
//                                    }
//                                }
//                                fiveSuccess=true
//                                if(currentSuccess == true)
//                                {
//                                    layout.visibility=View.VISIBLE
//                                    progress.visibility=View.GONE
//
//                                }
//                                Log.i("TAG", "onViewCreated4:${newList} ")
//
//                            }
//                            is ApiState.Failure ->{
//                                Toast.makeText(requireActivity(),"Failed",Toast.LENGTH_SHORT).show()
//                                it.msg.printStackTrace()
//                            }
//                            else -> {
//                                Toast.makeText(requireActivity(),"Loading",Toast.LENGTH_SHORT).show()
//                                layout.visibility=View.GONE
//                                progress.visibility=View.VISIBLE
//                            }
//                        }
//                    }
//            }
//
//        }


    }
    private fun isSameDay(timestamp: Long) : Boolean{
        val calendar=Calendar.getInstance()
        calendar.timeInMillis=timestamp*1000
        Log.i("TAG", "isSameDay: ${calendar.time}")
        return Calendar.getInstance().get(Calendar.DATE) == calendar.get(Calendar.DATE)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    private fun setUiComponents(weatherResponse: WeatherResponse)
    {
        val forecast = weatherResponse.current
        Log.i("TAG", "setUiComponents: $forecast")
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
    private fun hideUi()
    {

    }
    fun timestampToDate(timestamp: Long): Date {
        // Convert seconds to milliseconds
        val milliseconds = timestamp * 1000
        return Date(milliseconds)
    }
    private fun getLocation()
    {
        val fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationRequest=  LocationRequest.Builder(5000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            setMaxUpdates(1)
        }.build()
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(location: LocationResult) {
                Log.i("TAG", "onLocationResult: ${location} ")
                currentLon=location.lastLocation?.longitude
                currentLat=location.lastLocation?.latitude
                viewModel.getForecastResponse(currentLat!!,currentLon!!, API_KEY)
                val editor = sharedPreferences.edit()
                editor.putFloat("currentLon",currentLon!!.toFloat())
                editor.putFloat("currentLat",currentLat!!.toFloat())
                editor.apply()
            }
        }
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            requireActivity().requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var result = true
        for(per in grantResults)
        {
            if(per != PackageManager.PERMISSION_GRANTED) {
                result=false
            }
        }
        if(!result)
        {
            requireActivity().requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1)
        }
    }
}
