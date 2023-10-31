package com.weather.app.data

import com.weather.app.data.model.FavoriteWeatherUiModel
import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.data.source.local.dao.WeatherDao
import com.weather.app.data.source.local.entity.FavoriteWeatherEntity
import com.weather.app.data.source.remote.dto.ForecastDto
import com.weather.app.data.source.remote.dto.WeatherDto
import com.weather.app.data.source.remote.service.ApiService
import com.weather.app.di.IoDispatcher
import com.weather.app.util.ApiResult
import com.weather.app.util.Mapper
import com.weather.app.util.handleApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    @IoDispatcher
    private val dispatcherIO: CoroutineDispatcher,
    private val apiService: ApiService,
    private val dao: WeatherDao,
    private val weatherMapper: Mapper<WeatherDto, WeatherUiModel>,
    private val forecastMapper: Mapper<ForecastDto, ForecastUiModel>,
    private val favoriteMapper: Mapper<List<@JvmSuppressWildcards FavoriteWeatherEntity>, List<@JvmSuppressWildcards FavoriteWeatherUiModel>>
) : WeatherRepository {

    override suspend fun getWeatherData(cityName: String): ApiResult<WeatherUiModel> {
        return handleApi(weatherMapper) {
            apiService.getWeatherData(cityName = cityName)
        }
    }

    override suspend fun getForecastData(cityName: String): ApiResult<ForecastUiModel> {
        return handleApi(forecastMapper) {
            apiService.getForecastData(cityName = cityName)
        }
    }

    override suspend fun insertFavorite(id: Int, name: String) {
        withContext(dispatcherIO) {
            dao.insert(FavoriteWeatherEntity(id, name))
        }
    }

    override suspend fun deleteFavorite(id: Int, name: String) {
        withContext(dispatcherIO) {
            dao.delete(FavoriteWeatherEntity(id, name))
        }
    }

    override suspend fun getFavorites(): Flow<List<FavoriteWeatherUiModel>> {
        return withContext(dispatcherIO) {
            dao.getFavorites().map {
                favoriteMapper.map(it)
            }
        }
    }
}