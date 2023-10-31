package com.weather.app.data.model

data class WeatherUiModel(
    val id: Int,
    val name: String,
    val dt: Long,
    val coord: WeatherCoordUiModel,
    val weatherDetail: WeatherDetailUiModel,
    val main: WeatherMainUiModel,
    val wind: WeatherWindUiModel,
)

data class WeatherCoordUiModel(
    val lat: Double,
    val lon: Double
)