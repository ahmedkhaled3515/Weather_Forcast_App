package com.example.weatherapp.model

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.FakeLocalDataSource
import com.example.weatherapp.FakeRemoteDataSource
import com.example.weatherapp.database.AlertDao
import com.example.weatherapp.database.AppDatabase
import com.example.weatherapp.database.ILocalDataSource
import com.example.weatherapp.database.LocalDataSource
import com.example.weatherapp.network.IRemoteDataSource
import com.example.weatherapp.network.RemoteDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar


class AppRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var localDataSource: ILocalDataSource
    private lateinit var remoteDataSource: IRemoteDataSource
    private lateinit var appRepository: IAppRepository
    @Before
    fun setup(){
        val favorites = mutableListOf<FavoriteCoordinate>()
        val alerts = mutableListOf<LocationAlert>()
        localDataSource = FakeLocalDataSource(favorites,alerts)
        remoteDataSource = FakeRemoteDataSource()
        appRepository = AppRepository.getInstance(remoteDataSource, localDataSource)
    }
    @Test
    fun addFavoriteAndGetAllFavoritesTest_addTwoFavorite_sizeEqualTwo() = runBlockingTest{
        //Given
        val favoriteCoordinate = FavoriteCoordinate(longitude = 0.0, latitude = 0.0)
        val favoriteCoordinate2 = FavoriteCoordinate(longitude = 0.2, latitude = 0.3)
        appRepository.addFavorite(favoriteCoordinate)
        appRepository.addFavorite(favoriteCoordinate2)
        //When
        val size = appRepository.getAllFavorites().first().size
        //Then
        Assert.assertEquals(2,size)
    }
    @Test
    fun deleteFavoriteTest_addTwoDeleteOne_sizeEqualOne() = runBlockingTest {
        //Given
        val favoriteCoordinate1 = FavoriteCoordinate(longitude = 0.0, latitude = 0.0)
        val favoriteCoordinate2 = FavoriteCoordinate(longitude = 2.0, latitude = 1.0)
        //When
//        Log.i("TAG", "deleteFavoriteTest_addTwoDeleteOne_sizeEqualOne: $favoriteCoordinate1")
        appRepository.addFavorite(favoriteCoordinate1)
        appRepository.addFavorite(favoriteCoordinate2)
        appRepository.deleteFavorite(favoriteCoordinate1)
        //Then
        val job = async() {
            appRepository.getAllFavorites().first().size
        }
        val size = job.await()
        Assert.assertEquals(1,size)
    }
    @Test
    fun addAlertAndGetAllAlertsTest_addTwoAlert_sizeEqualTwo() = runBlockingTest {
        //Given
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 55
        val locationAlert = LocationAlert(longitude = 0.0, latitude = 0.0, calendar =  Calendar.getInstance())
        val locationAlert2 = LocationAlert(longitude = 2.0, latitude = 4.0, calendar = calendar)
        //When
        appRepository.addAlert(locationAlert)
        appRepository.addAlert(locationAlert2)
//        appRepository.deleteAlert(locationAlert2)
        //Then
        val size = appRepository.getAllAlerts().first().size
        Assert.assertEquals(2,size)
    }
    @Test
    fun deleteAlertTest_addTwoDeleteOneAlert_sizeEqualOne() = runBlockingTest {
        //Given
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 55
        val locationAlert = LocationAlert(5,0.0,0.0, Calendar.getInstance())
        val locationAlert2 = LocationAlert(6,2.0,4.0,calendar)
        //When
        appRepository.addAlert(locationAlert)
        appRepository.addAlert(locationAlert2)
        appRepository.deleteAlert(locationAlert)
//        appRepository.deleteAlert(locationAlert2)
        //Then
        val size = appRepository.getAllAlerts().first().size
        Assert.assertEquals(1,size)
    }
    @Test
    fun getLastInsertedRowTest_addOneDeleteWithId1_returnId1() = runBlockingTest {
        val locationAlert2 = LocationAlert(longitude = 2.0, latitude = 4.0, calendar =  Calendar.getInstance())
        appRepository.addAlert(locationAlert2)
        val index = appRepository.getLastInsertedAlert().first()
        Assert.assertEquals(locationAlert2,index)
    }

    @After
    fun tearDown() {
    }
}