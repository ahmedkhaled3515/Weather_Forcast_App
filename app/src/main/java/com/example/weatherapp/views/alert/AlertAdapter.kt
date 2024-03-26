package com.example.weatherapp.views.alert

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.AlertCardLayoutBinding
import com.example.weatherapp.model.LocationAlert

class AlertAdapter(val context: Context , val remove : (location: LocationAlert) -> Unit) : ListAdapter<LocationAlert, AlertAdapter.ViewHolder>(
    LocationAlertDiffUtil()
) {
    class ViewHolder(val binding: AlertCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlertCardLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val currentLocation : LocationAlert= getItem(position)
        val calendar = currentLocation.calendar
        val address  = coordToString(currentLocation)
        binding.alertLocationName.text = "${address?.locality}, ${address?.adminArea}, ${address?.countryName}"
        binding.dateText.text = calendar.time.toString()
        binding.alertRemoveButton.setOnClickListener(){
            remove(currentLocation)
        }
    }
    fun coordToString(locationAlert: LocationAlert) : Address?
    {
        var address: Address?=null
        val geocoder= Geocoder(context)

        val addresses = geocoder.getFromLocation(locationAlert.latitude, locationAlert.longitude, 1)
        if (addresses != null) {
            if (addresses.isNotEmpty()) {
                address = addresses[0]
            }
        }
        return address

    }
}
class LocationAlertDiffUtil : DiffUtil.ItemCallback<LocationAlert>(){
    override fun areItemsTheSame(oldItem: LocationAlert, newItem: LocationAlert): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationAlert, newItem: LocationAlert): Boolean {
        return oldItem == newItem
    }

}