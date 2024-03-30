package com.example.weatherapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import java.util.Calendar

@ExperimentalCoroutinesApi
class AlertViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var appRepo: IAppRepository
    private lateinit var alertViewModel: AlertViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appRepo = FakeAppRepository()
        alertViewModel = AlertViewModel(appRepo)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun addAlertAndGetAllAlertsTest_addOneAlert_returnSize1() = testScope.runBlockingTest {
        // Given
        val locationAlert = LocationAlert(longitude = 0.0, latitude = 0.0, calendar = Calendar.getInstance())
        alertViewModel.addAlert(locationAlert)
        // When
        val size = alertViewModel.alertFlow.first().size
        // Then
        Assert.assertEquals(1, size)
    }

    @Test
    fun deleteAlertTest_addOneDeleteOne_returnSize0() = testScope.runBlockingTest {
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
