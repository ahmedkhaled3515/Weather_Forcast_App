package com.example.weatherapp.views.map

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.AlertViewModel
import com.example.weatherapp.ViewModel.AlertViewModelFactory
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.ViewModel.CurrentForecastViewModelFactory
import com.example.weatherapp.ViewModel.FavoriteViewModel
import com.example.weatherapp.ViewModel.FavouriteViewModelFactory
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.databinding.FragmentMapsBinding
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.RemoteDataSource
import com.example.weatherapp.sevices.DismissNotificationReceiver
import com.example.weatherapp.workers.AlertWorker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MapsFragment : Fragment(),OnMapReadyCallback{

    private lateinit var gMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var coordinates : LatLng
    private lateinit var navController : NavController
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var favoriteViewModel:FavoriteViewModel
    private var incomingBundle: Bundle?=null
    private lateinit var calendar: Calendar
    private lateinit var alertViewModel : AlertViewModel
    private var currentLatitude : Double?=null
    private var currentLongitude: Double? =null
    private lateinit var viewModel: CurrentForecastViewModel
    private lateinit var notificationWeather : WeatherResponse

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE)
        coordinates = LatLng(sharedPreferences.getFloat("currentLat",0F).toDouble(),sharedPreferences.getFloat("currentLon",0F).toDouble())
        calendar = Calendar.getInstance()
        incomingBundle = arguments as Bundle
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, CurrentForecastViewModelFactory(
            AppRepository.getInstance(
                RemoteDataSource(), LocalDataSource(
            AppDatabase.getInstance(requireContext()))
            ))
        ).get(CurrentForecastViewModel::class.java)
        favoriteViewModel = ViewModelProvider(this,FavouriteViewModelFactory(AppRepository.getInstance(RemoteDataSource(),LocalDataSource(
            AppDatabase.getInstance(requireContext()))))).get(FavoriteViewModel::class.java)
        alertViewModel = ViewModelProvider(this,AlertViewModelFactory(AppRepository.getInstance(RemoteDataSource(),LocalDataSource(
            AppDatabase.getInstance(requireContext()))))).get(AlertViewModel::class.java)
        navController = Navigation.findNavController(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(map: GoogleMap) {
        gMap=map
        // Set a default location (e.g., your current location or a specific location)
        val defaultLocation = LatLng(37.7749, -122.4194)
        gMap.addMarker(MarkerOptions().position(defaultLocation).title("Marker in San Francisco"))
        gMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))

        // Set a listener to handle map clicks
        gMap.setOnMapClickListener { latLng ->
            // Handle map click
            gMap.clear() // Clear existing markers
            gMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            Log.i("TAG", "onMapReady: $latLng")
            coordinates = latLng
        }
        binding.markerFab.setOnClickListener(){
            Log.i("TAG", "onMapReady: $coordinates")
            val bundle = Bundle().apply {
                putFloat("lat", coordinates.latitude.toFloat())
                putFloat("lon", coordinates.longitude.toFloat())
            }
            currentLatitude = coordinates.latitude
            currentLongitude = coordinates.longitude
            val newFav=FavoriteCoordinate(longitude = currentLongitude!!, latitude = currentLatitude!!)
            if(incomingBundle?.getString("sourceFragment") == "favorite")
            {
                favoriteViewModel.addFavorite(newFav)
                navController.navigate(R.id.action_mapsFragment_to_favoriteFragment,bundle)
            }else{
                openDateDialog()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDateDialog()
    {
        val datePickerDialog = DatePickerDialog(requireContext(),
            {
            view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR,year)
                calendar.set(Calendar.MONTH,month)
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                openTimeDialog()
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate= System.currentTimeMillis()
        datePickerDialog.show()
    }
    var count = 0
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openTimeDialog() {
        val currentTime = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                if (selectedTime.after(currentTime)) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    viewLifecycleOwner.lifecycleScope.launch{
                        alertViewModel.alertFlow.collect(){
                            if(it.isNotEmpty())
                            {
                                count = it.last().id
                                Log.i("TAG", "openTimeDialog: $it")
                            }
                        }
                    }
                    val locationAlert = LocationAlert(
                        id = count+1,
                        longitude = currentLongitude!!,
                        latitude = currentLatitude!!,
                        calendar = calendar
                    )
                    Log.i("TAG", "openTimeDialog: $locationAlert")
                    alertViewModel.addAlert(locationAlert)
                    setAlarm(currentLongitude!!, currentLatitude!!, calendar.timeInMillis,locationAlert)
                    navController.navigate(R.id.action_mapsFragment_to_alertFragment)
                } else {
                    Toast.makeText(requireContext(), "Can't pick passed time", Toast.LENGTH_SHORT).show()
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }
    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(long:Double, lat:Double, alertTime: Long, locationAlert: LocationAlert)
    {
        Log.i("TAG", "setAlarm: $locationAlert")
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = Intent(context,DismissNotificationReceiver::class.java)
        alarmIntent.apply {
            putExtra("id",locationAlert.id)
            putExtra("lat",lat)
            putExtra("long",long)
        }
        val pendingIntent = PendingIntent.getBroadcast(context,locationAlert.id,alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP,alertTime,pendingIntent)
        Log.i("TAG", "setAlarm: helloo")
    }
    private fun makeWorkRequest(long:Double,lat:Double,alertTime: Long,locationAlert: LocationAlert)
    {
        Log.i("TAG", "makeWorkRequest: $locationAlert")
        val currentTime = Calendar.getInstance().timeInMillis
        val timeDiff = alertTime/1000 - currentTime/1000
        val inputData = Data.Builder()
            .putDouble("long",currentLongitude!!)
            .putDouble("lat",currentLatitude!!)
            .putInt("id",locationAlert.id)
            .build()
//        val alertWorkRequest : WorkRequest = OneTimeWorkRequest.from(AlertWorker::class.java)
        val alertWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<AlertWorker>()
            .setInitialDelay(timeDiff,TimeUnit.SECONDS)
            .setInputData(inputData)
            .build()
        val workManger : WorkManager = WorkManager.getInstance(requireContext())
        workManger.enqueue(alertWorkRequest)
        workManger.getWorkInfoByIdLiveData(alertWorkRequest.id).observe(requireActivity(), Observer {
            workInfo ->
            if (workInfo != null) {
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        Log.i("TAG", "Work enqueued")
                    }
                    WorkInfo.State.RUNNING -> {
                        Log.i("TAG", "Work running")
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Log.i("TAG", "Work succeeded")
                        Log.i("TAG", "makeWorkRequest: $locationAlert")
                    }
                    WorkInfo.State.FAILED -> {
                        Log.i("TAG", "Work failed")
                    }
                    WorkInfo.State.CANCELLED -> {
                        Log.i("TAG", "Work cancelled")
                    }
                    else -> {
                        Log.i("TAG", "Work state unknown")
                    }
                }
            } else {
                Log.i("TAG", "WorkInfo is null")
            }
        })
    }
}