package com.example.weatherapp.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.AlertViewModel
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class AlertFragment : Fragment() {
    private lateinit var binding: FragmentAlertBinding
    private val alertViewModel : AlertViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButtonAction()
        setRecyclerView()
    }
    private fun addButtonAction()
    {
        binding.addAlertFab.setOnClickListener(){
            val bundle = Bundle()
            bundle.putString("sourceFragment" , "alert")
            findNavController().navigate(R.id.action_alertFragment_to_mapsFragment,bundle)
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
                        adapter=AlertAdapter(context, ::remove).apply {
                            submitList(it)
                        }
                    }
                }
            }
        }
    }
    private fun remove(locationAlert: LocationAlert){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                alertViewModel.deleteAlert(locationAlert)
            }
        }
    }
}