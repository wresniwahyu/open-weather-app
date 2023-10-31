package com.weather.app.data.source.remote.service

import com.weather.app.BuildConfig
import com.weather.app.data.source.remote.dto.ForecastDto
import com.weather.app.data.source.remote.dto.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val WEATHER_API_VERSION = "data/2.5/"

        private const val WEATHER = WEATHER_API_VERSION + "weather"
        private const val FORECAST = WEATHER_API_VERSION + "forecast"

        private const val QUERY_UNIT_METRIC = "metric"
    }

    @GET(WEATHER)
    suspend fun getWeatherData(
        @Query("q") cityName: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("units") unit: String = QUERY_UNIT_METRIC,
        @Query("appid") appId: String = BuildConfig.API_KEY
    ): Response<WeatherDto>

    @GET(FORECAST)
    suspend fun getForecastData(
        @Query("q") cityName: String? = null,
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("units") unit: String = QUERY_UNIT_METRIC,
        @Query("appid") appId: String = BuildConfig.API_KEY
    ): Response<ForecastDto>

}