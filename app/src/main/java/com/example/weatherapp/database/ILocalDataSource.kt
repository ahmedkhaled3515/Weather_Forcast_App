package com.example.weatherapp.database

import com.example.weatherapp.model.FavoriteCoordinate
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun getAllFavorites() : Flow<List<FavoriteCoordinate>>
    suspend fun addFavorite(coordinate: FavoriteCoordinate)
    suspend fun deleteFavorite(coordinate: FavoriteCoordinate)
}
