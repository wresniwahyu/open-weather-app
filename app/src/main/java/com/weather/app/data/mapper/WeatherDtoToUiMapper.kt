package com.weather.app.data.mapper

import com.weather.app.data.model.WeatherCoordUiModel
import com.weather.app.data.model.WeatherDetailUiModel
import com.weather.app.data.model.WeatherMainUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.data.model.WeatherWindUiModel
import com.weather.app.data.source.remote.dto.WeatherDetailDto
import com.weather.app.data.source.remote.dto.WeatherDto
import com.weather.app.data.source.remote.dto.WeatherMainDto
import com.weather.app.data.source.remote.dto.WeatherWindDto
import com.weather.app.util.Mapper
import javax.inject.Inject

class WeatherDtoToUiMapper @Inject constructor(
    private val weatherDetailMapper: Mapper<WeatherDetailDto?, WeatherDetailUiModel>,
    private val weatherMainMapper: Mapper<WeatherMainDto?, WeatherMainUiModel>,
    private val weatherWindMapper: Mapper<WeatherWindDto?, WeatherWindUiModel>
): Mapper<WeatherDto, WeatherUiModel> {
    override fun map(input: WeatherDto): WeatherUiModel {
        return WeatherUiModel(
            id = input.id ?: 0,
            name = input.name.orEmpty(),
            dt = input.dt ?: 0,
            coord = WeatherCoordUiModel(
                lat = input.coord?.lat ?: 0.0,
                lon = input.coord?.lon ?: 0.0,
            ),
            weatherDetail = weatherDetailMapper.map(input.weatherDetail),
            main = weatherMainMapper.map(input.main),
            wind = weatherWindMapper.map(input.wind)
        )
    }
}