package com.weather.app.data

import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.util.ApiResult

interface WeatherRepository {
    suspend fun getWeatherData(
        lat: Double,
        lon: Double
    ): ApiResult<WeatherUiModel>

    suspend fun getForecastData(
        lat: Double? = null,
        lon: Double? = null,
        cityName: String? = null
    ): ApiResult<ForecastUiModel>

}