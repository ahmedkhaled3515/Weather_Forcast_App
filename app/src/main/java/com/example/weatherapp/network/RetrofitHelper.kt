package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        private final val BASE_URL2="https://api.openweathermap.org/data/3.0/"
        private final val BASE_URL="https://api.openweathermap.org/data/2.5/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
object Api {
    val apiServices by lazy {
        RetrofitHelper.retrofit.create(ApiServices::class.java)
    }
}