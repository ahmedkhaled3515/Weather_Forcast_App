package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse

class RemoteDataSource : IRemoteDataSource {
    private val apiServices=Api.apiServices
    override suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String): Forecast
    {
        return apiServices.getCurrentForecast(lat,lon,apiKey)
    }

    override suspend fun getFiveDayForecast(lat: Double, lon: Double, apiKey: String) : ForecastResponse {
        return apiServices.getFiveDayForecast(lat,lon,apiKey)
    }
}