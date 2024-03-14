package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse

interface IRemoteDataSource {
    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String):Forecast
    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): ForecastResponse

}
