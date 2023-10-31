package com.weather.app.data.mapper

import com.weather.app.data.model.FavoriteWeatherUiModel
import com.weather.app.data.source.local.entity.FavoriteWeatherEntity
import com.weather.app.util.Mapper
import javax.inject.Inject

class FavoriteWeatherEntityToUiMapper @Inject constructor() :
    Mapper<List<@JvmSuppressWildcards FavoriteWeatherEntity>, List<@JvmSuppressWildcards FavoriteWeatherUiModel>> {
    override fun map(input: List<FavoriteWeatherEntity>): List<FavoriteWeatherUiModel> {
        return input.map {
            FavoriteWeatherUiModel(it.id, it.name)
        }
    }
}