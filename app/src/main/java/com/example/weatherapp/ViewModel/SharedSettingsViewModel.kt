package com.example.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Language
import kotlin.math.log

class SharedSettingsViewModel : ViewModel() {
    private val _language = MutableSharedFlow<String>(replay = 1)
    val language : SharedFlow<String> = _language.asSharedFlow()
    private val _unitsFlow = MutableSharedFlow<String>(replay = 1)
    val unitsFlow : SharedFlow<String> = _unitsFlow.asSharedFlow()
    private val _location = MutableSharedFlow<String>(replay = 1)
    val location : SharedFlow<String> = _location.asSharedFlow()
    fun changeLanguage(lang : String)
    {
        viewModelScope.launch {
            Log.i("TAG", "changeLanguage: $lang ")
            _language.emit(lang)
        }
    }
    fun changeUnit(units : String)
    {
        viewModelScope.launch {
            Log.i("TAG", "changeUnit: $units ")
            _unitsFlow.emit(units)
        }
    }
    fun changeLocation(type : String)
    {
        viewModelScope.launch {
            Log.i("TAG", "changeLocation: $type")
            _language.emit(type)
        }
    }

}