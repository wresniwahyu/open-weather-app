package com.weather.app.data

import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.data.source.remote.dto.ForecastDto
import com.weather.app.data.source.remote.dto.WeatherDto
import com.weather.app.data.source.remote.service.ApiService
import com.weather.app.di.IoDispatcher
import com.weather.app.util.ApiResult
import com.weather.app.util.Mapper
import com.weather.app.util.handleApi
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @IoDispatcher
    private val dispatcherIo: CoroutineDispatcher,
    private val apiService: ApiService,
    private val weatherMapper: Mapper<WeatherDto, WeatherUiModel>,
    private val forecastMapper: Mapper<ForecastDto, ForecastUiModel>
) : WeatherRepository {

    override suspend fun getWeatherData(
        lat: Double,
        lon: Double
    ): ApiResult<WeatherUiModel> {
        return handleApi(weatherMapper) {
            apiService.getWeatherData(lat = lat, lon = lon)
        }
    }

    override suspend fun getForecastData(cityName: String): ApiResult<ForecastUiModel> {
        return handleApi(forecastMapper) {
            apiService.getForecastData(cityName = cityName)
        }
    }
}