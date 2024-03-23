package com.example.weatherapp.model

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.ILocalDataSource
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.network.IRemoteDataSource
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

class AppRepository private constructor(val context: Context): IAppRepository {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: AppRepository? = null

        fun getInstance(context: Context) : AppRepository {
            return INSTANCE ?: synchronized(this)
            {
                val instance = AppRepository(context)
                INSTANCE =instance
                instance

            }
        }
    }
    private var remoteDataSource: IRemoteDataSource = RemoteDataSource()
    private var localDataSource : ILocalDataSource = LocalDataSource(context)
//    override suspend fun getCurrentForecast(lat:Double, lon:Double, apiKey:String): Flow<Forecast>
//    {
//        return remoteDataSource.getCurrentForecast(lat,lon,apiKey)
//    }
    override suspend fun getFiveDayForecast(lat:Double, lon:Double, apiKey:String): Flow<WeatherResponse>
    {
        return remoteDataSource.getFiveDayForecast(lat,lon,apiKey)
    }
    override suspend fun getDailyForecast(lat: Double, lon: Double, apiKey: String) : WeatherResponse
    {
        return remoteDataSource.getDailyForecast(lat,lon,apiKey)
    }

    override suspend fun getAllForecastData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Flow<WeatherResponse> {
        return remoteDataSource.getAllForecastData(lat, lon, apiKey)
    }

    override suspend fun getAllFavorites(): Flow<List<FavoriteCoordinate>> {
        return localDataSource.getAllFavorites()
    }

    override suspend fun addFavorite(coordinate: FavoriteCoordinate) {
        localDataSource.addFavorite(coordinate)
    }

    override suspend fun deleteFavorite(coordinate: FavoriteCoordinate) {
        localDataSource.deleteFavorite(coordinate)
    }

    override suspend fun getAllAlerts(): Flow<List<LocationAlert>> {
        return localDataSource.getAllAlerts()
    }

    override suspend fun addAlert(locationAlert: LocationAlert) {
        localDataSource.addAlert(locationAlert)
    }

    override suspend fun deleteAlert(locationAlert: LocationAlert) {
        localDataSource.deleteAlert(locationAlert)
    }

    override suspend fun deleteAlertById(id: Int) {
        localDataSource.deleteAlertById(id)
    }

}