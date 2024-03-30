package com.example.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.IAppRepository

class CurrentForecastViewModelFactory(private val appRepo: IAppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrentForecastViewModel::class.java)) {
            return CurrentForecastViewModel(appRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}