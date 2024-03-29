package com.example.weatherapp.model

import com.example.weatherapp.network.IRemoteDataSource
import com.example.weatherapp.network.RemoteDataSource
import okhttp3.ResponseBody

object AppRepository {
    var remoteDataSource: IRemoteDataSource = RemoteDataSource()
    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String):Forecast
    {
        return remoteDataSource.getCurrentForecast(lat,lon,apiKey)
    }
    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String):ForecastResponse
    {
        return remoteDataSource.getFiveDayForecast(lat,lon,apiKey)
    }
    suspend fun getOneCall(lat: Double,lon: Double,apiKey: String) : ResponseBody
    {
        return remoteDataSource.getOneCall(lat,lon,apiKey)
    }

}