package com.example.weatherapp.views.alert

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.AlertViewModel
import com.example.weatherapp.ViewModel.AlertViewModelFactory
import com.example.weatherapp.ViewModel.CurrentForecastViewModel
import com.example.weatherapp.ViewModel.CurrentForecastViewModelFactory
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.LocationAlert
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.launch


class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private lateinit var alertViewModel : AlertViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertViewModel = ViewModelProvider(this, AlertViewModelFactory(
            AppRepository.getInstance(
                RemoteDataSource(), LocalDataSource(
            AppDatabase.getInstance(requireContext()))
            ))
        ).get(AlertViewModel::class.java)
        addButtonAction()
        setRecyclerView()
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun addButtonAction()
    {
        binding.addAlertFab.setOnClickListener(){
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS),2)
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            else
            {
                val bundle = Bundle()
                bundle.putString("sourceFragment" , "alert")
                findNavController().navigate(R.id.action_alertFragment_to_mapsFragment,bundle)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            requireActivity().requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),1)
        }
    }
    private fun setRecyclerView()
    {
        binding.alertRV.apply {
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                alertViewModel.alertFlow.collect(){
                    binding.alertRV.apply {
                        adapter= AlertAdapter(context, ::remove).apply {
                            submitList(it)
                        }
                    }
                }
            }
        }
    }
    private fun remove(locationAlert: LocationAlert){
        alertViewModel.deleteAlert(locationAlert)
    }

}