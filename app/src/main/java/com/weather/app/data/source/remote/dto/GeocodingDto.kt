package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class GeocodingDto(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
    @SerializedName("country")
    val country: Double? = null,
    @SerializedName("state")
    val state: Double? = null
)
