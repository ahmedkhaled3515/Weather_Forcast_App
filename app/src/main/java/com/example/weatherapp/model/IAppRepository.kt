package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow

interface IAppRepository {
    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String): Flow<Forecast>

    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): Flow<ForecastResponse>

    suspend fun getDailyForecast(lat: Double,lon: Double,apiKey: String) : ForecastResponse
    suspend fun getAllFavorites() : Flow<List<FavoriteCoordinate>>
    suspend fun addFavorite(coordinate: FavoriteCoordinate)
    suspend fun deleteFavorite(coordinate: FavoriteCoordinate)
}