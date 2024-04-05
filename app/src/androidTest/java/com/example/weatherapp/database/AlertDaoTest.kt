package com.example.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar
@SmallTest
class AlertDaoTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private lateinit var roomDB: AppDatabase
    private lateinit var alertDao : AlertDao
    @Before
    fun setup(){
        roomDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java)
            .build()
        alertDao = roomDB.alertDao()
    }
    @Test
    fun addAlert_addTwoLocationAlert_listSizeTwo()
    {
        runBlocking {
            //Given
            val locationAlert = LocationAlert(0,0.0,0.0, Calendar.getInstance())
            val locationAlert2 = LocationAlert(1,0.0,0.0, Calendar.getInstance())
            alertDao.addAlert(locationAlert2)
            alertDao.addAlert(locationAlert)
            //When
            var size = 0
            val job =async {

                alertDao.getAllAlerts().first().size
            }
            size = job.await()
            //Then
            Assert.assertEquals(2,size)
        }
    }
    @Test
    fun getAllAlerts_noLocations_zeroSizeList()
    {
        runBlocking {
            //Given

            //When
            var size = 0
            val job =async {

                alertDao.getAllAlerts().first().size
            }
            size = job.await()
            //Then
            Assert.assertEquals(0,size)
        }
    }
//    @Test
//    fun deleteAlert_deleteOneAlert_listSizeOne()
//    {
//        runBlocking {
//            // Given
//            val locationAlert = LocationAlert(0, 0.0, 0.0, Calendar.getInstance())
//            val locationAlert2 = LocationAlert(1, 1.0, 2.0, Calendar.getInstance())
//            alertDao.addAlert(locationAlert2)
//            alertDao.addAlert(locationAlert)
//            alertDao.deleteAlert(locationAlert)
//
//            // When
//            val job = async {
//                alertDao.getAllAlerts().first().size
//            }
//            val size = job.await()
//            // Then
//            assertEquals(1,size)
//        }
//    }
    @After
    fun tearDown(){
        roomDB.close()
    }
}