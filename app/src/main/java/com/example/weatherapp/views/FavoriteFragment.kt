package com.example.weatherapp.views

import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.FavoriteViewModel
import com.example.weatherapp.databinding.FragmentFavoriteBinding
import com.example.weatherapp.model.FavoriteCoordinate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment() {
    private lateinit var navController : NavController
    private lateinit var binding: FragmentFavoriteBinding
    private val favoriteViewModel : FavoriteViewModel by activityViewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
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
        navController = Navigation.findNavController(view)
        binding.addFab.setOnClickListener {
            navController.navigate(R.id.action_favoriteFragment_to_mapsFragment)
        }
        setRecyclerView()
    }
    private fun setRecyclerView()
    {
        binding.favoriteRV.apply {
            layoutManager=LinearLayoutManager(this.context).apply {
                orientation=LinearLayoutManager.VERTICAL
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                favoriteViewModel.favoriteFlow.collect(){
                    binding.favoriteRV.apply {
                        adapter=FavoriteAdapter(requireContext(), ::removeLocation).apply {
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


}