package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse
import okhttp3.ResponseBody

interface IRemoteDataSource {
    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String):Forecast
    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): ForecastResponse
    suspend fun getOneCall(lat: Double,lon: Double,apiKey: String):ResponseBody
}
