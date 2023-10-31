package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherMainDto(
    @SerializedName("temp")
    val temp: Double? = null,
    @SerializedName("humidity")
    val humidity: Int? = null,
    @SerializedName("temp_min")
    val tempMin: Double? = null,
    @SerializedName("temp_max")
    val tempMax: Double? = null
)
