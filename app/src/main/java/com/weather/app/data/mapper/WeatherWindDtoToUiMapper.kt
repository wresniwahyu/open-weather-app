package com.weather.app.data.mapper

import com.weather.app.data.model.WeatherWindUiModel
import com.weather.app.data.source.remote.dto.WeatherWindDto
import com.weather.app.util.Mapper
import javax.inject.Inject

class WeatherWindDtoToUiMapper @Inject constructor(): Mapper<WeatherWindDto?, WeatherWindUiModel> {
    override fun map(input: WeatherWindDto?): WeatherWindUiModel {
        return WeatherWindUiModel(
            speed = input?.speed ?: 0.0,
            deg = input?.deg ?: 0,
            gust = input?.gust ?: 0.0
        )
    }
}