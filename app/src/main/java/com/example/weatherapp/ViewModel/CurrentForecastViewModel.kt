package com.example.weatherapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.ApiState
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class CurrentForecastViewModel private constructor() : ViewModel() {
    private val appRepo=AppRepository
    private var _forecast = MutableStateFlow<ApiState<Forecast>>(ApiState.Loading)
    var forecast = _forecast.asStateFlow()
    private var _fiveForecast = MutableStateFlow<ApiState<ForecastResponse>>(ApiState.Loading)
    var fiveForecast = _fiveForecast.asStateFlow()
    private var _oneCall: MutableLiveData<ResponseBody> = MutableLiveData()
    var oneCall:LiveData<ResponseBody> = _oneCall
    fun getCurrentForecast(lat:Double,lon:Double,apiKey:String)
    {
        viewModelScope.launch {
//            _forecast.postValue(appRepo.getCurrentForecast(lat,lon,apiKey))
            appRepo.getCurrentForecast(lat,lon,apiKey)
                .catch {
                    _forecast.value = ApiState.Failure(it)
                }
                .collect(){
                _forecast.value=ApiState.Success(it)
            }
        }
    }
    fun getFiveForecast(lat:Double,lon:Double,apiKey:String)
    {
        viewModelScope.launch {
//            _fiveForecast.postValue(appRepo.getFiveDayForecast(lat,lon,apiKey))
            appRepo.getFiveDayForecast(lat,lon,apiKey)
                .catch {
                    _fiveForecast.value= ApiState.Failure(it)
                }
                .collect(){
                    _fiveForecast.value= ApiState.Success(it)
                }
        }
    }
//    fun getOneCall(lat: Double,lon: Double,apiKey: String)
//    {
//        viewModelScope.launch(Dispatchers.IO) {
//            _oneCall.postValue(appRepo.getOneCall(lat,lon,apiKey))
//        }
//    }
}