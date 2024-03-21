package com.example.weatherapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import java.util.Calendar
import java.util.Date

@Database(entities = [FavoriteCoordinate::class,LocationAlert::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCoordinateDao() : FavoriteCoordinateDAO
    abstract fun alertDao() : AlertDao
    companion object{
        private var INSTANCE:AppDatabase? = null
        fun getInstance(context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this)
            {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,"appDb")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE =instance
                instance
            }
        }
    }

}

class Converters {
    @TypeConverter
    fun fromCalendar(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun toCalendar(timestamp: Long): Calendar {
        return Calendar.getInstance().apply { timeInMillis = timestamp }
    }
}