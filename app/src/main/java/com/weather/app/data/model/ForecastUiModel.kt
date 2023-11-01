package com.weather.app.data.model

import com.weather.app.screen.home.HomeGroupedForecastData
import com.weather.app.ui.component.ForecastGroupItemData
import com.weather.app.ui.component.ForecastItemData
import com.weather.app.util.formatDate

data class ForecastUiModel(
    val city: ForecastCityUiModel,
    val list: List<ForecastItemUiModel>,
) {
    fun getForecastGroupedByDate(): List<HomeGroupedForecastData> {
        return list.groupBy {
            it.dt.formatDate()
        }.map { (date, items) ->
            HomeGroupedForecastData(
                group = ForecastGroupItemData(
                    date = date,
                    minTemp = items.minByOrNull { it.main.temp }?.main?.temp ?: 0.0,
                    maxTemp = items.maxByOrNull { it.main.temp }?.main?.temp ?: 0.0
                ),
                groupItems = items.map { item ->
                    ForecastItemData(
                        date = item.dt.formatDate(),
                        humidity = item.main.humidity,
                        temp = item.main.temp,
                        wind = item.wind.speed,
                        icon = item.weather.icon
                    )
                }
            )
        }
    }
}

data class ForecastCityUiModel(
    val id: Int,
    val name: String,
)

data class ForecastItemUiModel(
    val dt: Long,
    val dtTxt: String,
    val weather: WeatherDetailUiModel,
    val main: WeatherMainUiModel,
    val wind: WeatherWindUiModel,
)
