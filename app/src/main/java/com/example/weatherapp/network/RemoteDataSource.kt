package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody

class RemoteDataSource : IRemoteDataSource {
    private val apiServices=Api.apiServices
    override suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String): Flow<Forecast>
    {
        return flow {
            val forecast= apiServices.getCurrentForecast(lat,lon,apiKey)
            emit(forecast)
        }
    }

    override suspend fun getFiveDayForecast(lat: Double, lon: Double, apiKey: String) : Flow<ForecastResponse> {
        return flow {
            val forecastResponse= apiServices.getFiveDayForecast(lat,lon,apiKey)
            emit(forecastResponse)
        }
    }

    override suspend fun getDailyForecast(lat: Double, lon: Double, apiKey: String): ForecastResponse {
        return apiServices.getDailyForecast(lat,lon,apiKey)
    }
}