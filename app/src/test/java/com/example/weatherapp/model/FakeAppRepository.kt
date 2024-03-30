package com.example.weatherapp.model

import com.example.weatherapp.FakeLocalDataSource
import com.example.weatherapp.FakeRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAppRepository : IAppRepository {
    private val fakeLocalDataSource : FakeLocalDataSource = FakeLocalDataSource(mutableListOf(),
        mutableListOf()
    )
    private val fakeRemoteDataSource : FakeRemoteDataSource = FakeRemoteDataSource()
    override suspend fun getFiveDayForecast(
        lat: Double,
        lon: Double,
        apiKey: String
    ): Flow<WeatherResponse> {
        return flow {

        }
    }

    override suspend fun getAllForecastData(
        lat: Double,
        lon: Double,
        apiKey: String,
        units: String,
        lang: String
    ): Flow<WeatherResponse> {
        return flow {

        }
    }

    override suspend fun getAllFavorites(): Flow<List<FavoriteCoordinate>> {
        return fakeLocalDataSource.getAllFavorites()
    }

    override suspend fun addFavorite(coordinate: FavoriteCoordinate) {
        return fakeLocalDataSource.addFavorite(coordinate)
    }

    override suspend fun deleteFavorite(coordinate: FavoriteCoordinate) {
        return fakeLocalDataSource.deleteFavorite(coordinate)
    }

    override suspend fun getAllAlerts(): Flow<List<LocationAlert>> {
        return fakeLocalDataSource.getAllAlerts()
    }

    override suspend fun addAlert(locationAlert: LocationAlert) {
        fakeLocalDataSource.addAlert(locationAlert)
    }

    override suspend fun deleteAlert(locationAlert: LocationAlert): Int {
        return fakeLocalDataSource.deleteAlert(locationAlert)
    }

    override suspend fun deleteAlertById(id: Int): Int {
        return fakeLocalDataSource.deleteAlertById(id)
    }

    override suspend fun getLastInsertedAlert(): Flow<LocationAlert> {
        return fakeLocalDataSource.getLastInsertedAlert()
    }
}