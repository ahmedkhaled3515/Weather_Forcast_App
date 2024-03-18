package com.example.weatherapp.database

import android.content.Context
import com.example.weatherapp.model.FavoriteCoordinate
import kotlinx.coroutines.flow.Flow

class LocalDataSource(context: Context) : ILocalDataSource {
    private val appDatabase=AppDatabase.getInstance(context)
    private val favoriteCoordinateDAO = appDatabase.favoriteCoordinateDao()
    override suspend fun getAllFavorites() : Flow<List<FavoriteCoordinate>>{
        return favoriteCoordinateDAO.getAllFavorites()
    }
    override suspend fun addFavorite(coordinate: FavoriteCoordinate){
        favoriteCoordinateDAO.addToFavorites(coordinate)
    }
    override suspend fun deleteFavorite(coordinate: FavoriteCoordinate){
        favoriteCoordinateDAO.deleteFavorite(coordinate)
    }
}