package com.example.weatherapp

import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.network.IRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource : IRemoteDataSource {
    override suspend fun getFiveDayForecast(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Flow<WeatherResponse> {
        return flow {
        }
    }
    override suspend fun getAllForecastData(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Flow<WeatherResponse> {
        return flow {

        }
    }
}