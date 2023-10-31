package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("dt")
    val dt: Long? = null,
    @SerializedName("coord")
    val coord: WeatherCoordDto? = null,
    @SerializedName("weather")
    val weatherDetail: List<WeatherDetailDto>? = null,
    @SerializedName("main")
    val main: WeatherMainDto? = null,
    @SerializedName("wind")
    val wind: WeatherWindDto? = null,
)

data class WeatherCoordDto(
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null
)