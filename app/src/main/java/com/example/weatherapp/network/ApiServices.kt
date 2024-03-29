package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("weather")
    suspend fun getCurrentForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Forecast
    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("cnt") cnt:Int = 8
    ): ForecastResponse
    @GET("onecall")
    suspend fun getOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ):ResponseBody
}