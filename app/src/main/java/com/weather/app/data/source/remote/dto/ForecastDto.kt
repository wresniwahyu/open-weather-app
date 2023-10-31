package com.weather.app.data.source.remote.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("city")
    val city: ForecastCityDto? = null,
    @SerializedName("list")
    val list: List<ForecastItemDto>? = null,
)

data class ForecastCityDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
)

data class ForecastItemDto(
    @SerializedName("dt")
    val dt: Long? = null,
    @SerializedName("dt_txt")
    val dtTxt: String? = null,
    @SerializedName("weather")
    val weather: List<WeatherDetailDto>? = null,
    @SerializedName("main")
    val main: WeatherMainDto? = null,
    @SerializedName("wind")
    val wind: WeatherWindDto? = null,
)
