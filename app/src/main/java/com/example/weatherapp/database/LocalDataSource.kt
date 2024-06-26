package com.example.weatherapp.database

import android.content.Context
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val appDatabase : AppDatabase,
    ) : ILocalDataSource {
    private val favoriteCoordinateDAO = appDatabase.favoriteCoordinateDao()
    private val alertDao = appDatabase.alertDao()
    override suspend fun getAllFavorites() : Flow<List<FavoriteCoordinate>>{
        return favoriteCoordinateDAO.getAllFavorites()
    }
    override suspend fun addFavorite(coordinate: FavoriteCoordinate){
        favoriteCoordinateDAO.addToFavorites(coordinate)
    }
    override suspend fun deleteFavorite(coordinate: FavoriteCoordinate){
        favoriteCoordinateDAO.deleteFavorite(coordinate)
    }

    override suspend fun getAllAlerts(): Flow<List<LocationAlert>> {
        return alertDao.getAllAlerts()
    }

    override suspend fun addAlert(locationAlert: LocationAlert){
        alertDao.addAlert(locationAlert)
    }

    override suspend fun deleteAlert(locationAlert: LocationAlert): Int {
        return alertDao.deleteAlert(locationAlert)
    }

    override suspend fun deleteAlertById(id: Int) : Int{
        return alertDao.deleteAlertById(id)
    }

    override suspend fun getLastInsertedAlert() : Flow<LocationAlert> {
       return alertDao.getLastInsertedRow()
    }
}