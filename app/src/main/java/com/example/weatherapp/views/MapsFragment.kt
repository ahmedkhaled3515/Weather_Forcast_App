package com.example.weatherapp.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.AlertViewModel
import com.example.weatherapp.ViewModel.FavoriteViewModel
import com.example.weatherapp.databinding.FragmentMapsBinding
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Calendar
import kotlin.time.Duration.Companion.minutes

class MapsFragment : Fragment(),OnMapReadyCallback{

    private lateinit var gMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var coordinates : LatLng
    private lateinit var navController : NavController
    private lateinit var sharedPreferences: SharedPreferences
    private val favoriteViewModel:FavoriteViewModel by activityViewModels()
    private var incomingBundle: Bundle?=null
    private lateinit var calendar: Calendar
    private val alertViewModel : AlertViewModel by activityViewModels()
    private var currentLatitude : Double?=null
    private var currentLongitude: Double? =null
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
        navController = Navigation.findNavController(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

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
        datePickerDialog.show()
    }
    private fun openTimeDialog()
    {
        val timePickerDialog = TimePickerDialog(requireContext(),
            {
                view,hourOfDay,minute ->
                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
                    calendar.set(Calendar.MINUTE,minute)
                    val locationAlert = LocationAlert(longitude = currentLongitude!!, latitude = currentLatitude!!, calendar = calendar)
                Log.i("TAG", "openTimeDialog: $locationAlert")
                    alertViewModel.addAlert(locationAlert)
                    navController.navigate(R.id.action_mapsFragment_to_alertFragment)
            },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false)
        timePickerDialog.show()
    }
}