package com.example.weatherapp.database

import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun getAllFavorites() : Flow<List<FavoriteCoordinate>>
    suspend fun addFavorite(coordinate: FavoriteCoordinate)
    suspend fun deleteFavorite(coordinate: FavoriteCoordinate)
    suspend fun  getAllAlerts() : Flow<List<LocationAlert>>
    suspend fun  addAlert(locationAlert: LocationAlert)
    suspend fun deleteAlert(locationAlert: LocationAlert)
    suspend fun deleteAlertById(id: Int)
    suspend fun getLastInsertedAlert() : Flow<LocationAlert>
}
