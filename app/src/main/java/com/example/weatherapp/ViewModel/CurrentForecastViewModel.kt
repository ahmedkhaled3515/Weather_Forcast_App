package com.example.weatherapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentForecastViewModel private constructor() : ViewModel() {
    private val appRepo=AppRepository
    private var _forecast: MutableLiveData<Forecast> = MutableLiveData()
    var forecast:LiveData<Forecast> = _forecast
    private var _fiveForecast: MutableLiveData<ForecastResponse> = MutableLiveData()
    var fiveForecast:LiveData<ForecastResponse> = _fiveForecast
    fun getCurrentForecast(lat:Double,lon:Double,apiKey:String)
    {
        viewModelScope.launch  (Dispatchers.IO){
            _forecast.postValue(appRepo.getCurrentForecast(lat,lon,apiKey))
        }
    }
    fun getFiveForecast(lat:Double,lon:Double,apiKey:String)
    {
        viewModelScope.launch (Dispatchers.IO){
            _fiveForecast.postValue(appRepo.getFiveDayForecast(lat,lon,apiKey))
        }
    }
}