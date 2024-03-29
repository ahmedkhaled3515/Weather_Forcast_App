package com.example.weatherapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule


class LocalDataSourceTest {
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
    @After
    fun tearDown() {
    }
}