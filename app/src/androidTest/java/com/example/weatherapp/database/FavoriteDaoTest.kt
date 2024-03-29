package com.example.weatherapp.database

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapp.model.FavoriteCoordinate
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteDaoTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var roomDB: AppDatabase
    private lateinit var favoriteCoordinateDAO: FavoriteCoordinateDAO
    @Before
    fun setup(){
        roomDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java)
            .build()
        favoriteCoordinateDAO = roomDB.favoriteCoordinateDao()
    }
    @Test
    fun addFavoriteTest_addTwoFavorite_returnTwo()
    {
        runBlocking {
            //Given
            val favorite1 = FavoriteCoordinate(0,0.0,0.0)
            val favorite2 = FavoriteCoordinate(1,1.0,2.0)
            //When
            favoriteCoordinateDAO.addToFavorites(favorite2)
            favoriteCoordinateDAO.addToFavorites(favorite1)
            var size = 0
            val job =async {
                Log.i("TAG", "addFavorite_addTwoFavorite_returnTwo: ${favoriteCoordinateDAO.getAllFavorites().first()}")
                favoriteCoordinateDAO.getAllFavorites().first().size
            }
            size = job.await()
            //Then
            Assert.assertEquals(2,size)
        }
    }
    @Test
    fun deleteFavoriteTest_deleteOneFavorite_returnOne()
    {
        runBlocking {
            //Given
            val favorite1 = FavoriteCoordinate(0,0.0,0.0)
            val favorite2 = FavoriteCoordinate(1,1.0,2.0)
            //When
            favoriteCoordinateDAO.addToFavorites(favorite2)
            favoriteCoordinateDAO.addToFavorites(favorite1)
            favoriteCoordinateDAO.deleteFavorite(favorite2)
            var size = 0
            val job =async {
//                Log.i("TAG", "addFavorite_addTwoFavorite_returnTwo: ${favoriteCoordinateDAO.getAllFavorites().first()}")
                favoriteCoordinateDAO.getAllFavorites().first().size
            }
            size = job.await()
            //Then
            Assert.assertEquals(1,size)
        }
    }
    @Test
    fun getAllFavoriteTest_addingNoting_returnZero()
    {
        runBlocking {
            //Given
            //When
            val job =async {
                Log.i("TAG", "addFavorite_addTwoFavorite_returnTwo: ${favoriteCoordinateDAO.getAllFavorites().first()}")
                favoriteCoordinateDAO.getAllFavorites().first().size
            }
            val size = job.await()
            //Then
            Assert.assertEquals(0,size)
        }
    }

    @After
    fun tearDown(){
        roomDB.close()
    }
}