package com.weather.app.data.model

data class ForecastUiModel(
    val city: ForecastCityUiModel,
    val list: List<ForecastItemUiModel>,
)

data class ForecastCityUiModel(
    val id: Int,
    val name: String,
)

data class ForecastItemUiModel(
    val dt: Long,
    val dtTxt: String,
    val weather: WeatherDetailUiModel,
    val main: WeatherMainUiModel,
    val wind: WeatherWindUiModel,
)
