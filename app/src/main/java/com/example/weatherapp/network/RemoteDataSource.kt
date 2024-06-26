package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource : IRemoteDataSource {
    private val apiServices=Api.apiServices
//    override suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String): Flow<Forecast>
//    {
//        return flow {
//            val forecast= apiServices.getCurrentForecast(lat,lon,apiKey)
//            emit(forecast)
//        }
//    }

    override suspend fun getFiveDayForecast(lat: Double, lon: Double, apiKey: String) : Flow<WeatherResponse> {
        return flow {
            val forecastResponse= apiServices.getFiveDayForecast(lat,lon,apiKey)
            emit(forecastResponse)
        }
    }


    override suspend fun getAllForecastData(
        lat: Double,
        lon: Double,
        apiKey: String,
        units : String,
        lang : String
    ): Flow<WeatherResponse> {
        return flow {
            val forecastResponse = apiServices.getOneApi(lat, lon, apiKey,units,lang)
            emit(forecastResponse)
        }
    }
}