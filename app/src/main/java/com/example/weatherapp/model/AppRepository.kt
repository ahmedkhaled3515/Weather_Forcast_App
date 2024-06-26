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

class AppRepository private constructor(
    private var remoteDataSource: IRemoteDataSource,
    private var localDataSource : ILocalDataSource
    ): IAppRepository {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: AppRepository? = null

        fun getInstance(remoteDataSource: IRemoteDataSource,localDataSource: ILocalDataSource) : AppRepository {
            return INSTANCE ?: synchronized(this)
            {
                val instance = AppRepository(remoteDataSource,localDataSource)
                INSTANCE =instance
                instance

            }
        }
    }
//    override suspend fun getCurrentForecast(lat:Double, lon:Double, apiKey:String): Flow<Forecast>
//    {
//        return remoteDataSource.getCurrentForecast(lat,lon,apiKey)
//    }
    override suspend fun getFiveDayForecast(lat:Double, lon:Double, apiKey:String): Flow<WeatherResponse>
    {
        return remoteDataSource.getFiveDayForecast(lat,lon,apiKey)
    }


    override suspend fun getAllForecastData(
        lat: Double,
        lon: Double,
        apiKey: String,
        units:String,
        lang:String
    ): Flow<WeatherResponse> {
        return remoteDataSource.getAllForecastData(lat, lon, apiKey,units,lang)
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

    override suspend fun deleteAlert(locationAlert: LocationAlert): Int {
        return localDataSource.deleteAlert(locationAlert)
    }

    override suspend fun deleteAlertById(id: Int) : Int{
        return localDataSource.deleteAlertById(id)
    }

    override suspend fun getLastInsertedAlert() : Flow<LocationAlert> {
        return localDataSource.getLastInsertedAlert()
    }

}