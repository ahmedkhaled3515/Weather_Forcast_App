package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.FavoriteCoordinate
@Database(entities = [FavoriteCoordinate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCoordinateDao() : FavoriteCoordinateDAO
    companion object{
        private var INSTANCE:AppDatabase? = null
        fun getInstance(context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this)
            {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,"appDb").build()
                INSTANCE =instance
                instance
            }
        }
    }

}