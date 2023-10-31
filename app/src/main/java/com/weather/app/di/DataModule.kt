package com.weather.app.di

import com.weather.app.data.WeatherRepository
import com.weather.app.data.WeatherRepositoryImpl
import com.weather.app.data.mapper.ForecastDtoToUiMapper
import com.weather.app.data.mapper.WeatherDetailDtoToUiMapper
import com.weather.app.data.mapper.WeatherDtoToUiMapper
import com.weather.app.data.mapper.WeatherMainDtoToUiMapper
import com.weather.app.data.mapper.WeatherWindDtoToUiMapper
import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherDetailUiModel
import com.weather.app.data.model.WeatherMainUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.data.model.WeatherWindUiModel
import com.weather.app.data.source.remote.dto.ForecastDto
import com.weather.app.data.source.remote.dto.WeatherDetailDto
import com.weather.app.data.source.remote.dto.WeatherDto
import com.weather.app.data.source.remote.dto.WeatherMainDto
import com.weather.app.data.source.remote.dto.WeatherWindDto
import com.weather.app.util.Mapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    abstract fun bindsWeatherDetailDtoToUiMapper(
        mapper: WeatherDetailDtoToUiMapper
    ): Mapper<WeatherDetailDto?, WeatherDetailUiModel>

    @Binds
    abstract fun bindsWeatherMainDtoToUiMapper(
        mapper: WeatherMainDtoToUiMapper
    ): Mapper<WeatherMainDto?, WeatherMainUiModel>

    @Binds
    abstract fun bindsWeatherWindDtoToUiMapper(
        mapper: WeatherWindDtoToUiMapper
    ): Mapper<WeatherWindDto?, WeatherWindUiModel>

    @Binds
    abstract fun bindsWeatherDtoToUiMapper(
        mapper: WeatherDtoToUiMapper
    ): Mapper<WeatherDto, WeatherUiModel>

    @Binds
    abstract fun bindsForecastDtoToUiMapper(
        mapper: ForecastDtoToUiMapper
    ): Mapper<ForecastDto, ForecastUiModel>

}