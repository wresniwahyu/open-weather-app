package com.weather.app.screen.home

import com.weather.app.ui.component.ForecastGroupItemData
import com.weather.app.ui.component.ForecastItemData

data class HomeWeatherScreenData(
    val id: Int,
    val cityName: String,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val weatherDescription: String,
    val humidity: Int,
    val wind: Double
)

data class HomeGroupedForecastData(
    val group: ForecastGroupItemData,
    val groupItems: List<ForecastItemData>
)