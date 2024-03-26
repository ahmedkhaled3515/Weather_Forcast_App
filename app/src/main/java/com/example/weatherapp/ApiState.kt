package com.example.weatherapp

sealed class ApiState <out T>{
    data class Success<T>(val data : T) : ApiState<T>()
    data class Failure<T>(val msg : Throwable) : ApiState<T>()
    data object Loading : ApiState<Nothing>()
}