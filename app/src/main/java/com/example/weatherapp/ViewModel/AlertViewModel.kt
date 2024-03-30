package com.example.weatherapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.IAppRepository
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class AlertViewModel(private val appRepo: IAppRepository) : ViewModel() {
    private val _alertFlow = MutableStateFlow<List<LocationAlert>>(emptyList())
    val alertFlow = _alertFlow.asStateFlow()
    private val _lastAlertInserted:MutableStateFlow<LocationAlert> = MutableStateFlow(
        LocationAlert(0,0.0,0.0,
            Calendar.getInstance())
    )
    val lastAlertInserted: StateFlow<LocationAlert> get() = _lastAlertInserted
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
    fun getLastInsertedAlert()
    {
        viewModelScope.launch (Dispatchers.IO){
            appRepo.getLastInsertedAlert().collect(){
                _lastAlertInserted.value=it
            }
        }
    }
}