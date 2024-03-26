package com.example.weatherapp.network

import com.example.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
//    @GET("weather")
//    suspend fun getCurrentForecast(
//        @Query("lat") lat: Double,
//        @Query("lon") lon: Double,
//        @Query("appid") apiKey: String,
//        @Query("units") units: String = "metric"
//    ): Forecast
    @GET("forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): WeatherResponse
    @GET("forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("cnt") cnt:Int = 5
    ): WeatherResponse
    @GET("onecall")
    suspend fun getOneApi(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ) : WeatherResponse
}