package com.weather.app.data.mapper

import com.weather.app.data.model.WeatherDetailUiModel
import com.weather.app.data.source.remote.dto.WeatherDetailDto
import com.weather.app.util.Mapper
import javax.inject.Inject

class WeatherDetailDtoToUiMapper @Inject constructor(): Mapper<WeatherDetailDto?, WeatherDetailUiModel> {
    override fun map(input: WeatherDetailDto?): WeatherDetailUiModel {
        return WeatherDetailUiModel(
            id = input?.id ?: 0,
            main = input?.main.orEmpty(),
            description = input?.description.orEmpty(),
            icon = input?.icon.orEmpty()
        )
    }
}