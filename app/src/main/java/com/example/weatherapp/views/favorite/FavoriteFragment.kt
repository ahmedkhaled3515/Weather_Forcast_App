package com.example.weatherapp.views.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.NetworkUtils
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.ViewModel.CurrentForecastViewModelFactory
import com.example.weatherapp.ViewModel.FavoriteViewModel
import com.example.weatherapp.ViewModel.FavouriteViewModelFactory
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    private lateinit var navController : NavController
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favoriteViewModel : FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var network : NetworkUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        network = NetworkUtils
        val bundle = arguments
        if(bundle != null)
        {
            val long = bundle.getFloat("lon")
            val lat = bundle.getFloat("lat")
            Toast.makeText(requireContext(), "lon is $long lat is $lat", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favoriteViewModel = ViewModelProvider(this, FavouriteViewModelFactory(
            AppRepository.getInstance(
                RemoteDataSource(), LocalDataSource(
            AppDatabase.getInstance(requireContext()))
            ))
        ).get(FavoriteViewModel::class.java)
        navController = Navigation.findNavController(view)
        if (network.isInternetAvailable(requireContext()))
        {
            binding.addFab.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("sourceFragment","favorite")
                navController.navigate(R.id.action_favoriteFragment_to_mapsFragment,bundle)
            }
        }
        else{
            Toast.makeText(requireActivity(),"No Internet",Toast.LENGTH_SHORT).show()
        }
        setRecyclerView()
    }
    private fun setRecyclerView()
    {
        val locale = Locale.getDefault()
        val numberFormat = NumberFormat.getInstance(locale)
        binding.favoriteRV.apply {
            layoutManager= LinearLayoutManager(this.context).apply {
                orientation= LinearLayoutManager.VERTICAL
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                favoriteViewModel.favoriteFlow.collect(){
                    binding.favoriteRV.apply {
                        val text = if(it.size > 0)
                        {
                            "${getString(R.string.item_count)} ${numberFormat.format(it.size)}"
                        }
                        else
                        {
                            getString(R.string.no_item)
                        }
                        binding.itemCount.text = text
                        adapter= FavoriteAdapter(requireContext(), ::removeLocation,::goToDetails).apply {
                            submitList(it)
                        }
                    }
                }
            }
        }

    }
    private fun removeLocation(coordinate: FavoriteCoordinate)
    {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                favoriteViewModel.deleteFavorite(coordinate)
            }
        }
    }
    private fun goToDetails(coordinate: FavoriteCoordinate){
        val bundle = Bundle()
        bundle.putDouble("lat",coordinate.latitude)
        bundle.putDouble("long",coordinate.longitude)
        bundle.putString("type","favorite")
        navController.navigate(R.id.action_favoriteFragment_to_detailsFragment,bundle)

    }


}