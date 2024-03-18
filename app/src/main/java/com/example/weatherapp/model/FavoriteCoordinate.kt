package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coordinates")
data class FavoriteCoordinate(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val longitude:Double,
    val latitude:Double
    ) {
}