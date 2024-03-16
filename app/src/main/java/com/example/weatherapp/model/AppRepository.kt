package com.example.weatherapp.model

import com.example.weatherapp.network.IRemoteDataSource
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

object AppRepository {
    private var remoteDataSource: IRemoteDataSource = RemoteDataSource()
    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String): Flow<Forecast>
    {
        return remoteDataSource.getCurrentForecast(lat,lon,apiKey)
    }
    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): Flow<ForecastResponse>
    {
        return remoteDataSource.getFiveDayForecast(lat,lon,apiKey)
    }
    suspend fun getDailyForecast(lat: Double,lon: Double,apiKey: String) : ForecastResponse
    {
        return remoteDataSource.getDailyForecast(lat,lon,apiKey)
    }

}