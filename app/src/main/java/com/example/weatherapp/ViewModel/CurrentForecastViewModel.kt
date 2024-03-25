package com.example.weatherapp.ViewModel
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.ApiState
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
class CurrentForecastViewModel (application: Application) : AndroidViewModel(application) {
    private val appRepo=AppRepository.getInstance(application.applicationContext)
    private var _forecast = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    var forecast = _forecast.asStateFlow()
    private var _fiveForecast = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Loading)
    var fiveForecast = _fiveForecast.asStateFlow()
    private var _oneCall: MutableLiveData<ResponseBody> = MutableLiveData()
    var oneCall:LiveData<ResponseBody> = _oneCall
//    fun getCurrentForecast(lat:Double,lon:Double,apiKey:String)
//    {
//        viewModelScope.launch {
////            _forecast.postValue(appRepo.getCurrentForecast(lat,lon,apiKey))
//            appRepo.getCurrentForecast(lat,lon,apiKey)
//                .catch {
//                    _forecast.value = ApiState.Failure(it)
//                }
//                .collect(){
//                _forecast.value=ApiState.Success(it)
//            }
//        }
//    }
    fun getForecastResponse(lat: Double,lon: Double,apiKey: String,units:String,lang:String){
        viewModelScope.launch {
            appRepo.getAllForecastData(lat, lon, apiKey,units,lang)
                .catch {
                    _forecast.value= ApiState.Failure(it)
                }
                .collect(){
                    _forecast.value= ApiState.Success(it)
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