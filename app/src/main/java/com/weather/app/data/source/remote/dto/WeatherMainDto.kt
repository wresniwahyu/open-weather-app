package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherMainDto(
    @SerializedName("temp")
    val temp: Double? = null,
    @SerializedName("humidity")
    val humidity: Int? = null
)
