package com.example.weatherapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepository
import com.example.weatherapp.model.FavoriteCoordinate
import com.example.weatherapp.model.IAppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(private val appRepo: IAppRepository) : ViewModel() {
    private val _favoriteFlow : MutableStateFlow<List<FavoriteCoordinate>> = MutableStateFlow(emptyList())
    val favoriteFlow = _favoriteFlow.asStateFlow()
    init {
        getAllFavorite()
    }
    private fun getAllFavorite(){
        viewModelScope.launch {
            appRepo.getAllFavorites().collect()
            {
                _favoriteFlow.value = it
            }
        }
    }
    fun addFavorite(coordinate: FavoriteCoordinate)
    {
        viewModelScope.launch {
            appRepo.addFavorite(coordinate)
            getAllFavorite()
        }
    }
    fun deleteFavorite(coordinate: FavoriteCoordinate)
    {
        viewModelScope.launch {
            appRepo.deleteFavorite(coordinate)
            getAllFavorite()
        }
    }
}