package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface IRemoteDataSource {
//    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String):Flow<Forecast>
    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): Flow<WeatherResponse>
    suspend fun getDailyForecast(lat: Double,lon: Double,apiKey: String): WeatherResponse
    suspend fun getAllForecastData(lat: Double,lon: Double,apiKey: String,units:String = "metric", lang:String = "en") : Flow<WeatherResponse>
}
