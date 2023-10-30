package com.weather.app.data.source.remote.service

import com.weather.app.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val VERSION = "3.0/"
        private const val WEATHER_TIMEMACHINE = VERSION + "onecall/timemachine"
    }

    @GET(WEATHER_TIMEMACHINE)
    fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("dt") dt: Int,
        @Query("units") unit: String,
        @Query("appid") appId: String = BuildConfig.API_KEY
    ): Response<Unit>

}