package com.weather.app.screen.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.weather.app.base.BaseViewModel
import com.weather.app.data.WeatherRepository
import com.weather.app.data.model.FavoriteWeatherUiModel
import com.weather.app.data.model.ForecastItemUiModel
import com.weather.app.di.IoDispatcher
import com.weather.app.ui.component.ForecastGroupItemData
import com.weather.app.ui.component.ForecastItemData
import com.weather.app.util.formatDate
import com.weather.app.util.onError
import com.weather.app.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeWeatherViewModel @Inject constructor(
    @IoDispatcher
    private val dispatcherIo: CoroutineDispatcher,
    private val repository: WeatherRepository
) : BaseViewModel<HomeWeatherViewModel.State, HomeWeatherViewModel.Event>() {

    private val _event = MutableSharedFlow<Event>()
    val event = _event.asSharedFlow()

    private var searchJob: Job? = null

    fun searchByName(name: String) {
        viewModelScope.launch {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(1000)
                getWeatherData(name = name)
                getForecastData(name = name)
                getFavorites()
            }
        }
    }

    private fun getWeatherData(name: String) {
        viewModelScope.launch {
            repository.getWeatherData(
                cityName = name
            ).onSuccess {
                _state.update { state ->
                    state.copy(
                        data = HomeWeatherScreenData(
                            id = it.id,
                            cityName = it.name,
                            temp = it.main.temp,
                            tempMin = it.main.tempMin,
                            tempMax = it.main.tempMax,
                            weatherDescription = it.weatherDetail.description,
                            humidity = it.main.humidity,
                            wind = it.wind.speed
                        )
                    )
                }
            }
        }
    }

    private fun getForecastData(name: String) {
        viewModelScope.launch {
            repository.getForecastData(cityName = name).onSuccess {
                _state.update { state ->
                    state.copy(
                        groupedForecasts = it.list.groupByDate()
                    )
                }
            }.onError { e, code, message, error ->
                Log.e("e", e?.message.orEmpty())
            }
        }
    }

    private fun List<ForecastItemUiModel>.groupByDate(): List<HomeGroupedForecastData> {
        return this.groupBy {
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
                        wind = item.wind.speed
                    )
                }
            )
        }
    }

    fun selectForecastItem(items: List<ForecastItemData>) {
        _state.update { state ->
            state.copy(
                selectedForecastGroupItems = items
            )
        }
    }

    fun addToFavorite(id: Int, name: String) {
        viewModelScope.launch {
            if (_state.value.isFavorite) {
                repository.deleteFavorite(id, name)
            } else {
                repository.insertFavorite(id, name)
            }
            getFavorites()
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            repository.getFavorites()
                .catch {
                    // do something
                }
                .collectLatest {
                    _state.update { state ->
                        state.copy(
                            favorites = it,
                            isFavorite = it.checkIsFavorite(_state.value.data.id)
                        )
                    }
                }
        }
    }

    private fun List<FavoriteWeatherUiModel>.checkIsFavorite(id: Int): Boolean {
        return this.firstOrNull { it.id == id } != null
    }

    override fun defaultState() = State(
        data = HomeWeatherScreenData(
            id = 0,
            cityName = "",
            temp = 0.0,
            tempMin = 0.0,
            tempMax = 0.0,
            weatherDescription = "",
            humidity = 0,
            wind = 0.0
        ),
        isFavorite = false,
        groupedForecasts = listOf(),
        selectedForecastGroupItems = listOf(),
        favorites = listOf()
    )

    override fun onEvent(event: Event) {
        viewModelScope.launch(dispatcherIo) {
            when (event) {
                is Event.ShowError -> _event.emit(Event.ShowError(event.throwable))
            }
        }
    }

    data class State(
        val data: HomeWeatherScreenData,
        val isFavorite: Boolean,
        val groupedForecasts: List<HomeGroupedForecastData>,
        val selectedForecastGroupItems: List<ForecastItemData>,
        val favorites: List<FavoriteWeatherUiModel>
    )

    sealed class Event {
        class ShowError(val throwable: Throwable) : Event()
    }

}