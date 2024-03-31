package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.weatherapp.model.WeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

const val CHANNEL_ID = "1"
const val NOTIFICATION_ID = 1
object WeatherSharedPreferences {

    private const val PREF_NAME = "WeatherPrefs"
    private const val KEY_WEATHER_RESPONSE = "weatherResponse"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveWeatherResponse(context: Context, weatherResponse: WeatherResponse) {
        val gson = Gson()
        val json = gson.toJson(weatherResponse)
        val editor = getSharedPreferences(context).edit()
        editor.putString(KEY_WEATHER_RESPONSE, json)
        editor.apply()
    }

    fun getWeatherResponse(context: Context): WeatherResponse? {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(KEY_WEATHER_RESPONSE, null)
        return gson.fromJson(json, WeatherResponse::class.java)
    }

    fun clearWeatherResponse(context: Context) {
        val editor = getSharedPreferences(context).edit()
        editor.remove(KEY_WEATHER_RESPONSE)
        editor.apply()
    }
}
object NetworkUtils {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        }
    }
}
