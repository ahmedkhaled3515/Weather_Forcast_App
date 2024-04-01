package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences

var language : String = "en"
var units : String = "metric"
object SettingsSharedPreferences {

    private const val PREF_NAME = "SettingsPref"
    private const val LANGUAGE = "language"
    private const val UNITS = "units"
    private const val LOCATION = "location"
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveLanguage(context: Context,lang : String) {

        val editor = getSharedPreferences(context).edit()
        editor.putString(LANGUAGE,lang)
        editor.apply()
    }

    fun getLanguage(context: Context): String? {
        return getSharedPreferences(context).getString(LANGUAGE, null)
    }
    fun saveUnits(context: Context,units : String) {

        val editor = getSharedPreferences(context).edit()
        editor.putString(UNITS,units)
        editor.apply()
    }

    fun getUnits(context: Context): String? {
        return getSharedPreferences(context).getString(UNITS, null)
    }
    fun saveLocation(context: Context,location : String) {

        val editor = getSharedPreferences(context).edit()
        editor.putString(LOCATION,location)
        editor.apply()
    }

    fun getLocations(context: Context): String? {
        return getSharedPreferences(context).getString(LOCATION, null)
    }
}