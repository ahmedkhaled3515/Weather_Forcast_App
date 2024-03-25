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

    fun changeLanguage(lang : String)
    {
        viewModelScope.launch {
            Log.i("TAG", "changeLanguage: $lang ")
            _language.emit(lang)
        }
    }

}