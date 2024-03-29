package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.model.FavoriteCoordinate
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCoordinateDAO {
    @Query("select * from favorite_coordinates")
    fun getAllFavorites() : Flow<List<FavoriteCoordinate>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorites(coordinate : FavoriteCoordinate)
    @Delete
    suspend fun deleteFavorite(coordinate: FavoriteCoordinate)
}