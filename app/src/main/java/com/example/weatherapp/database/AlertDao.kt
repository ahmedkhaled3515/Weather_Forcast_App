package com.example.weatherapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("select * from location_alert")
    fun getAllAlerts() : Flow<List<LocationAlert>>
    @Insert
    fun addAlert(locationAlert: LocationAlert)
    @Delete
    fun deleteAlert(locationAlert: LocationAlert)
}