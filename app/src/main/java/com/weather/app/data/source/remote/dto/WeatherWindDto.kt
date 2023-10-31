package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherWindDto(
    @SerializedName("speed")
    val speed: Double? = null,
    @SerializedName("deg")
    val deg: Int? = null,
    @SerializedName("gust")
    val gust: Double? = null
)