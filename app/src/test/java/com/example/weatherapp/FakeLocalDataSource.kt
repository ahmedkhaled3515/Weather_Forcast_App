package com.example.weatherapp

import android.util.Log
import com.example.weatherapp.database.ILocalDataSource
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(
    private val favorites: MutableList<FavoriteCoordinate>,
    private val alerts: MutableList<LocationAlert>
) : ILocalDataSource {

    private var alertIdCounter = 0

    override suspend fun getAllFavorites(): Flow<List<FavoriteCoordinate>> {
        return flow<List<FavoriteCoordinate>> {
            emit(favorites)
        }
    }

    override suspend fun addFavorite(coordinate: FavoriteCoordinate) {
        favorites.add(coordinate)
    }

    override suspend fun deleteFavorite(coordinate: FavoriteCoordinate) {
        favorites.remove(coordinate)
    }

    override suspend fun getAllAlerts(): Flow<List<LocationAlert>> {
        return flow {
            emit(alerts)
        }
    }

    override suspend fun addAlert(locationAlert: LocationAlert) {
        // Ensure that the ID is unique
        if (locationAlert.id == 0) {
            locationAlert.id = ++alertIdCounter
        }
        // Add alert only if it's not already in the list
        if (!alerts.contains(locationAlert)) {
            alerts.add(locationAlert)
        } else {
        }
    }

    override suspend fun deleteAlert(locationAlert: LocationAlert): Int {
        alerts.remove(locationAlert)
        return locationAlert.id
    }

    override suspend fun deleteAlertById(id: Int): Int {
        val alertToRemove = alerts.find { it.id == id }
        if (alertToRemove != null) {
            alerts.remove(alertToRemove)
            return id
        }
        return -1
    }

    override suspend fun getLastInsertedAlert(): Flow<LocationAlert> {
        return flow {
            emit(alerts.last())
        }
    }
}
