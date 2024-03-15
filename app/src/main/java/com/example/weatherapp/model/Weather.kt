package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list:List<Forecast>
)
data class Forecast(
    val coord:Coordinates,
    val weather: List<Weather>,
    val base:String,
    val main: Main,
    val visibility:Int,
    val wind: Wind,
    val rain: Rain,
    val cloud: Cloud,
    val dt:Long,
    val sys: System,
    val timezone:Int,
    val id:Long,
    val name:String,
    val cod:Int,
    @SerializedName("dt_txt")
    val dateText:String
) {//
}
data class Coordinates(
    val lon:Float,
    val lat:Float
)
data class Weather(
    val id:Long,
    val main:String,
    val description:String,
    val icon:String
)
data class Main(
    val temp:Float,
    @SerializedName("feels_like")
    val feelsLike:Float,
    @SerializedName("temp_min")
    val tempMin:Float,
    @SerializedName("temp_max")
    val tempMax:Float,
    val pressure:Float,
    val humidity:Float,
    @SerializedName("sea_level")
    val seaLevel:Float,
    @SerializedName("grnd_level")
    val grndLevel:Float
)
data class Wind(
    val speed:Float,
    val deg:Float,
    val gust:Float
)
data class Rain(
    @SerializedName("1h")
    val oneHourVolume:Float,
    @SerializedName("3h")
    val threeHourVolume:Float
)
data class Cloud(
    val all:Float
)
data class System(
    val type:Int,
    val id:Long,
    val country:String,
    val sunrise:Long,
    val sunset:Long
)