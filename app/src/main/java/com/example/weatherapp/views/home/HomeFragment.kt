package com.example.weatherapp.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.weatherapp.ApiState
import com.example.weatherapp.MainActivity2
import com.example.weatherapp.R
import com.example.weatherapp.SettingsSharedPreferences
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.ViewModel.CurrentForecastViewModelFactory
import com.example.weatherapp.ViewModel.SharedSettingsViewModel
import com.example.weatherapp.WeatherSharedPreferences
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.databinding.NavHeaderMainBinding
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    companion object{
          const val API_KEY="172e0cbb3264b27530f5b6c425ffb29d"
    }
//    private var language = "en"
    private var language : String? = "en"
    private var units : String? = "metric"
    private lateinit var screen: ScrollView
    var currentLat:Double?=null
    var currentLon:Double?=null
    private lateinit var weatherIcon:ImageView
    private lateinit var currentDegreeTV:TextView
    private lateinit var currentDescriptionTV:TextView
    private lateinit var windSpeedTV:TextView
    private lateinit var rainChanceTV:TextView
    private lateinit var pressureTV:TextView
    private lateinit var humidityTV:TextView
    private lateinit var viewModel:CurrentForecastViewModel
    private lateinit var hourlyRV:RecyclerView
    private lateinit var dailyRV:RecyclerView
    private lateinit var locationText:TextView
    private var currentSuccess:Boolean?=false
    private lateinit var progress:LottieAnimationView
    private val sharedSettingsViewModel : SharedSettingsViewModel by activityViewModels()
    private lateinit var weatherSharedPreferences : WeatherSharedPreferences
    private lateinit var settingsSharedPreferences: SettingsSharedPreferences
    private lateinit var navView:NavigationView
    private lateinit var headerBinding : NavHeaderMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        headerBinding = NavHeaderMainBinding.inflate(bi)
        settingsSharedPreferences = SettingsSharedPreferences
//        settingsSharedPreferences.saveLanguage(requireContext(),"en")
//        settingsSharedPreferences.saveUnits(requireContext(),"metric")
        language = settingsSharedPreferences.getLanguage(requireContext())
        units = settingsSharedPreferences.getUnits(requireContext())
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this,CurrentForecastViewModelFactory(AppRepository.getInstance(RemoteDataSource(),LocalDataSource(
            AppDatabase.getInstance(requireContext()))))).get(CurrentForecastViewModel::class.java)
        val bundle = arguments
        if(bundle == null)
        {
            getLocation()

        }
        else
        {
            val type = bundle.getString("type")
            if(type == "maps")
            {
                currentLat = bundle.getFloat("lat").toDouble()
                currentLon = bundle.getFloat("long").toDouble()
                Log.i("TAG", "onCreateView: $currentLon , $currentLat")
                viewModel.getForecastResponse(currentLat!!,currentLon!!, API_KEY, units = units!!,language!!)
            }
            else{
                getLocation()
            }

        }
        weatherSharedPreferences = WeatherSharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("myShared",Context.MODE_PRIVATE)
//        navView = inflater.inflate(R.layout.nav_header_main,container,false)
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
        screen = view.findViewById(R.id.screen)
        progress=view.findViewById(R.id.lottieAnimationView)
        progress.playAnimation()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeComponents(view)

//        (activity as MainActivity2).updateActionBarTitle("Your Fragment Title")
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
                            screen.visibility =View.VISIBLE

                        }
                        is ApiState.Failure ->{
                            if(weatherSharedPreferences.getWeatherResponse(requireContext()) != null)
                            {
                                val weatherResponse =weatherSharedPreferences.getWeatherResponse(requireContext())
                                setUiComponents(weatherResponse!!)
                                Toast.makeText(requireActivity(),"No Internet Offline Mode",Toast.LENGTH_SHORT).show()
                                progress.visibility = View.GONE
                                screen.visibility =View.VISIBLE
                            }
                            else{
                                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                                it.msg.printStackTrace()

                            }

                        }
                        else -> {
                            progress.visibility = View.VISIBLE
                            screen . visibility = View.GONE
                            Toast.makeText(requireActivity(),"Loading",Toast.LENGTH_SHORT).show()
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
        val calendar=Calendar.getInstance()
        calendar.timeInMillis=timestamp*1000
        Log.i("TAG", "isSameDay: ${calendar.time}")
        return Calendar.getInstance().get(Calendar.DATE) == calendar.get(Calendar.DATE)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    private fun setUiComponents(weatherResponse: WeatherResponse)
    {
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getInstance(locale)
        val forecast = weatherResponse.current
        Log.i("TAG", "setUiComponents: $weatherResponse")
        var tempUnit="°c"
        var windUnit = "M/S"
        if(locale.language == "ar")
        {
            tempUnit="مئوية"
            windUnit = "متر/ثانية"
        }

        if (units == "metric")
        {
            currentDegreeTV.text="${numberFormat.format(forecast.temp)}°c"
            windSpeedTV.text="${numberFormat.format(forecast.windSpeed)}$windUnit"
        }
        else if (units == "imperial")
        {
            currentDegreeTV.text="${numberFormat.format(forecast.temp)}°F"
            windSpeedTV.text="${numberFormat.format(forecast.windSpeed)}M/H"
        }
        else{
            currentDegreeTV.text="${numberFormat.format(forecast.temp)}°K"
            windSpeedTV.text="${numberFormat.format(forecast.windSpeed)}M/S"
        }

        currentDescriptionTV.text=forecast.weather[0].description

        rainChanceTV.text= forecast.rain?.toString()?:getString(R.string.no_chance)
        pressureTV.text=numberFormat.format(forecast.pressure).toString()
        humidityTV.text=numberFormat.format(forecast.humidity).toString()
        locationText.text=weatherResponse.timezone
//        getAddressWithCoordinates(forecast.coord.lat.toDouble(),forecast.coord.lon.toDouble())
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
            .into(weatherIcon)
        drawerHeader()
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
//    private fun changeLanguage(languageCode: String) {
//        val locale = Locale(languageCode)
//        Locale.setDefault(locale)
//
//        val config = Configuration(resources.configuration)
//        config.setLocale(locale)
//
//        requireContext().resources.updateConfiguration(config, resources.displayMetrics)
//
//        // Recreate the activity to apply the language change
////        requireActivity().recreate()
//    }
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
                viewModel.getForecastResponse(currentLat!!,currentLon!!, API_KEY, units = units!!,language!!)
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
        else{
            requireActivity().recreate()
        }
    }
    private fun drawerHeader()
    {
        (activity as MainActivity2).updateNavigationHeader()
    }

}
