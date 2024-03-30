package com.example.weatherapp.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.model.FakeAppRepository
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.IAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)

class FavoriteViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var appRepo: IAppRepository
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        appRepo = FakeAppRepository()
        favoriteViewModel = FavoriteViewModel(appRepo)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun addAndGetAllFavoritesTest_addOneFavorite_returnSize1() = testScope.runBlockingTest{
        //Given
        val favoriteCoordinate = FavoriteCoordinate(longitude = 0.0, latitude = 0.0)
        favoriteViewModel.addFavorite(favoriteCoordinate)
        //When
        val size = favoriteViewModel.favoriteFlow.first().size
        //Then
        Assert.assertEquals(1,size)
    }
    @Test
    fun deleteFavoriteTest_addOneDeleteFavorite_returnSize0() = testScope.runBlockingTest{
        //Given
        val favoriteCoordinate = FavoriteCoordinate(longitude = 0.0, latitude = 0.0)
        favoriteViewModel.addFavorite(favoriteCoordinate)
        //When
        favoriteViewModel.deleteFavorite(favoriteCoordinate)
        val size = favoriteViewModel.favoriteFlow.first().size
        //Then
        Assert.assertEquals(0,size)
    }

}