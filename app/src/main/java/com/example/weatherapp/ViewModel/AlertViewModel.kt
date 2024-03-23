package com.example.weatherapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertViewModel(application: Application) : AndroidViewModel(application) {
    val appRepo = AppRepository.getInstance(application.applicationContext)
    private val _alertFlow = MutableStateFlow<List<LocationAlert>>(emptyList())
    val alertFlow = _alertFlow.asStateFlow()
    init {
        getAllAlerts()
    }
    private fun getAllAlerts()
    {
        viewModelScope.launch {
            appRepo.getAllAlerts().collect(){
                _alertFlow.value = it
            }
        }

    }
    fun addAlert(locationAlert: LocationAlert) {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.addAlert(locationAlert)
        }
        getAllAlerts()
    }
    fun deleteAlert(locationAlert: LocationAlert)
    {
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.deleteAlert(locationAlert)
        }
        getAllAlerts()
    }
    fun deleteAlertById(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            appRepo.deleteAlertById(id)
        }
        getAllAlerts()
    }
}