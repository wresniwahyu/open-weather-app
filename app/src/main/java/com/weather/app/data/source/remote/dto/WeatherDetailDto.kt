package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherDetailDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("main")
    val main: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("icon")
    val icon: String? = null
)
