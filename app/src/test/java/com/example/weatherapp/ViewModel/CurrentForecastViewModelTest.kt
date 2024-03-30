package com.example.weatherapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.FakeAppRepository
import com.example.weatherapp.model.IAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)

class CurrentForecastViewModelTest{
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


}