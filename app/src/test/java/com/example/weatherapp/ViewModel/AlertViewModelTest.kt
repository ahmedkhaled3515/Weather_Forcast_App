package com.example.weatherapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.ViewModel.AlertViewModel
import com.example.weatherapp.model.FakeAppRepository
import com.example.weatherapp.model.IAppRepository
import com.example.weatherapp.model.LocationAlert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var appRepo: IAppRepository
    private lateinit var alertViewModel: AlertViewModel


    @Before
    fun setUp() {
        appRepo = FakeAppRepository()
        alertViewModel = AlertViewModel(appRepo)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addAlertAndGetAllAlertsTest_addOneAlert_returnSize1() = runBlockingTest {
        // Given
        val locationAlert = LocationAlert(longitude = 0.0, latitude = 0.0, calendar = Calendar.getInstance())
        alertViewModel.addAlert(locationAlert)
        // When
        val size = alertViewModel.alertFlow.first().size
        // Then
        Assert.assertEquals(1, size)
    }

    @Test
    fun deleteAlertTest_addOneDeleteOne_returnSize0() = runBlockingTest {
        // Given
        val locationAlert = LocationAlert(longitude = 0.0, latitude = 0.0, calendar = Calendar.getInstance())
        alertViewModel.addAlert(locationAlert)
        // When
        alertViewModel.deleteAlert(locationAlert)
        val size = alertViewModel.alertFlow.first().size
        // Then
        Assert.assertEquals(0, size)
    }

}
