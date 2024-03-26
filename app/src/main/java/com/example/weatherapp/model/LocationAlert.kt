package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.Calendar

@Entity(tableName = "location_alert")
data class LocationAlert(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val longitude:Double,
    val latitude:Double,
    val calendar: Calendar
) {

}