package com.weather.app.data.mapper

import com.weather.app.data.model.ForecastCityUiModel
import com.weather.app.data.model.ForecastItemUiModel
import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherDetailUiModel
import com.weather.app.data.model.WeatherMainUiModel
import com.weather.app.data.model.WeatherWindUiModel
import com.weather.app.data.source.remote.dto.ForecastDto
import com.weather.app.data.source.remote.dto.WeatherDetailDto
import com.weather.app.data.source.remote.dto.WeatherMainDto
import com.weather.app.data.source.remote.dto.WeatherWindDto
import com.weather.app.util.Mapper
import javax.inject.Inject

class ForecastDtoToUiMapper @Inject constructor(
    private val weatherDetailMapper: Mapper<WeatherDetailDto?, WeatherDetailUiModel>,
    private val weatherMainMapper: Mapper<WeatherMainDto?, WeatherMainUiModel>,
    private val weatherWindMapper: Mapper<WeatherWindDto?, WeatherWindUiModel>
) : Mapper<ForecastDto, ForecastUiModel> {
    override fun map(input: ForecastDto): ForecastUiModel {
        return ForecastUiModel(
            city = ForecastCityUiModel(
                id = input.city?.id ?: 0, name = input.city?.name.orEmpty()
            ), list = input.list?.map {
                ForecastItemUiModel(
                    dt = it.dt ?: 0,
                    dtTxt = it.dtTxt.orEmpty(),
                    weather = weatherDetailMapper.map(it.weather),
                    main = weatherMainMapper.map(it.main),
                    wind = weatherWindMapper.map(it.wind)
                )
            }.orEmpty()
        )
    }
}