package com.example.weatherapp.views.favorite
import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FavoriteCardLayoutBinding
import com.example.weatherapp.model.FavoriteCoordinate
class FavoriteAdapter(val context: Context , val removeLocation: (coord:FavoriteCoordinate)-> Unit) : ListAdapter<FavoriteCoordinate, FavoriteAdapter.ViewHolder>(
    FavoriteCoordinateDiffUtil()
) {
    class ViewHolder(val binding: FavoriteCardLayoutBinding ) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteCardLayoutBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val favoriteCoordinate  = getItem(position)
        val address = coordToString(favoriteCoordinate,binding)
        binding.favoriteLocationName.text="${address?.locality}, ${address?.adminArea}, ${address?.countryName}"
        binding.favoritRemoveButton.setOnClickListener(){
            showAlertDialog(favoriteCoordinate)
        }
    }

    fun coordToString(favoriteCoordinate: FavoriteCoordinate,binding: FavoriteCardLayoutBinding) : Address?
    {
        var address: Address?=null
        val geocoder=Geocoder(context)

            val addresses = geocoder.getFromLocation(favoriteCoordinate.latitude, favoriteCoordinate.longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    address = addresses[0]
                }
            }
        return address

    }

class FavoriteCoordinateDiffUtil : DiffUtil.ItemCallback<FavoriteCoordinate>() {
    override fun areItemsTheSame(
        oldItem: FavoriteCoordinate,
        newItem: FavoriteCoordinate
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FavoriteCoordinate,
        newItem: FavoriteCoordinate
    ): Boolean {
        return oldItem == newItem
    }
}
    private fun showAlertDialog(favoriteCoordinate: FavoriteCoordinate) {
        AlertDialog.Builder(context)
            .setTitle("Alert Title")
            .setMessage("Are you sure you want to remove this location")
            .setPositiveButton("OK") { dialog, which ->
                removeLocation(favoriteCoordinate)
                // Handle positive button click
                // For example, perform some action on the clicked item
                // You can access your data using the position parameter
            }
            .setNegativeButton("Cancel", null) // You can handle cancel action if needed
            .show()
    }
}
