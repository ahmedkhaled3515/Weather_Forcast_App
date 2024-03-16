package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface IRemoteDataSource {
    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String):Flow<Forecast>
    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): Flow<ForecastResponse>
    suspend fun getDailyForecast(lat: Double,lon: Double,apiKey: String): ForecastResponse
}
