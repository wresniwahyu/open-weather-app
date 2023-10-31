package com.weather.app.data.mapper

import com.weather.app.data.model.WeatherMainUiModel
import com.weather.app.data.source.remote.dto.WeatherMainDto
import com.weather.app.util.Mapper
import javax.inject.Inject

class WeatherMainDtoToUiMapper @Inject constructor() : Mapper<WeatherMainDto?, WeatherMainUiModel> {
    override fun map(input: WeatherMainDto?): WeatherMainUiModel {
        return WeatherMainUiModel(
            temp = input?.temp ?: 0.0,
            humidity = input?.humidity ?: 0,
            tempMin = input?.tempMin ?: 0.0,
            tempMax = input?.tempMax ?: 0.0
        )
    }
}