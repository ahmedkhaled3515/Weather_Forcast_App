package com.example.weatherapp.model

import kotlinx.coroutines.flow.Flow

interface IAppRepository {
//    suspend fun getCurrentForecast(lat:Double,lon:Double,apiKey:String): Flow<Forecast>

    suspend fun getFiveDayForecast(lat:Double,lon:Double,apiKey:String): Flow<WeatherResponse>

    suspend fun getDailyForecast(lat: Double,lon: Double,apiKey: String) : WeatherResponse
    suspend fun getAllForecastData(lat: Double,lon: Double,apiKey: String,units: String = "metric",lang : String = "en") : Flow<WeatherResponse>
    suspend fun getAllFavorites() : Flow<List<FavoriteCoordinate>>
    suspend fun addFavorite(coordinate: FavoriteCoordinate)
    suspend fun deleteFavorite(coordinate: FavoriteCoordinate)
    suspend fun  getAllAlerts() : Flow<List<LocationAlert>>
    suspend fun  addAlert(locationAlert: LocationAlert)
    suspend fun deleteAlert(locationAlert: LocationAlert) : Int
    suspend fun deleteAlertById(id:Int) : Int
    suspend fun getLastInsertedAlert() : Flow<LocationAlert>
}