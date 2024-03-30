package com.example.weatherapp.database

import com.example.weatherapp.FakeLocalDataSource
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class LocalDataSourceTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var localDataSource: ILocalDataSource
    @Before
    fun setup(){
        localDataSource =
            com.example.weatherapp.FakeLocalDataSource(mutableListOf(), mutableListOf())
    }
    @Test
    fun addFavoriteAndGetAllFavoritesTest_addTwoFavorite_sizeEqualTwo() = runTest{
        //Given
        val favoriteCoordinate = FavoriteCoordinate(longitude = 0.0, latitude = 0.0)
        localDataSource.addFavorite(favoriteCoordinate)
        localDataSource.addFavorite(FavoriteCoordinate(longitude = 0.2, latitude = 0.3))
        //When
        val size = localDataSource.getAllFavorites().first().size
        //Then
        Assert.assertEquals(2,size)
    }
    @Test
    fun deleteFavoriteTest_addTwoDeleteOne_sizeEqualOne() = runTest {
        //Given
        val favoriteCoordinate1 = FavoriteCoordinate(longitude = 0.0, latitude = 0.0)
        val favoriteCoordinate2 = FavoriteCoordinate(longitude = 2.0, latitude = 1.0)
        //When
        localDataSource.addFavorite(favoriteCoordinate1)
        localDataSource.addFavorite(favoriteCoordinate2)
        localDataSource.deleteFavorite(favoriteCoordinate1)
        //Then
        val size = localDataSource.getAllFavorites().first().size
        Assert.assertEquals(1,size)
    }
    @Test
    fun addAlertAndGetAllAlertsTest_addTwoAlert_sizeEqualTwo() = runTest {
        //Given
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 55
        val locationAlert = LocationAlert(longitude = 0.0, latitude = 0.0, calendar = Calendar.getInstance())
        val locationAlert2 = LocationAlert(longitude = 2.0, latitude = 4.0, calendar = calendar)
        //When
        localDataSource.addAlert(locationAlert)
        localDataSource.addAlert(locationAlert2)
//        localDataSource.deleteAlert(locationAlert2)
        //Then
        val size = localDataSource.getAllAlerts().first().size
        Assert.assertEquals(2,size)
    }
    @Test
    fun deleteAlertTest_addTwoDeleteOneAlert_sizeEqualOne() = runTest {
        //Given
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 55
        val locationAlert = LocationAlert(longitude = 0.0, latitude = 0.0, calendar = Calendar.getInstance())
        val locationAlert2 = LocationAlert(longitude = 2.0, latitude = 4.0, calendar = calendar)
        //When
        localDataSource.addAlert(locationAlert)
        localDataSource.addAlert(locationAlert2)
        localDataSource.deleteAlert(locationAlert)
//        localDataSource.deleteAlert(locationAlert2)
        //Then
        val size = localDataSource.getAllAlerts().first().size
        Assert.assertEquals(1,size)
    }
    @Test
    fun getLastInsertedRowTest_addOneDeleteWithId1_returnId1() = runTest {
        val locationAlert2 = LocationAlert(longitude = 0.0, latitude = 0.0, calendar = Calendar.getInstance())
        localDataSource.addAlert(locationAlert2)
        val index = localDataSource.getLastInsertedAlert().first()
        Assert.assertEquals(locationAlert2,index)
    }
    @After
    fun tearDown() {
    }
}