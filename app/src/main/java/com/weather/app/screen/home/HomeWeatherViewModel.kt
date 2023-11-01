package com.weather.app.screen.home

import androidx.lifecycle.viewModelScope
import com.weather.app.base.BaseViewModel
import com.weather.app.data.WeatherRepository
import com.weather.app.data.model.FavoriteWeatherUiModel
import com.weather.app.di.IoDispatcher
import com.weather.app.ui.component.ForecastItemData
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
            }
        }
    }

    fun getWeatherData(name: String) {
        viewModelScope.launch {
            _state.update { state -> state.copy(isWeatherLoading = true) }
            repository.getWeatherData(
                cityName = name
            ).onSuccess {
                _state.update { state -> state.copy(isWeatherLoading = false) }
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
                getFavorites()
            }.onError { _, _, message, _ ->
                _state.update { state -> state.copy(isWeatherLoading = false) }
                _event.emit(Event.ShowNetworkError(message.orEmpty()))
            }
        }
    }

    fun getForecastData(name: String) {
        viewModelScope.launch {
            _state.update { state -> state.copy(isForecastLoading = true) }
            repository.getForecastData(cityName = name).onSuccess {
                _state.update { state ->
                    _state.update { state -> state.copy(isForecastLoading = false) }
                    state.copy(
                        groupedForecasts = it.getForecastGroupedByDate()
                    )
                }
            }.onError { _, _, message, _ ->
                _state.update { state -> state.copy(isForecastLoading = false) }
                _event.emit(Event.ShowNetworkError(message.orEmpty()))
            }
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

    fun getFavorites() {
        viewModelScope.launch {
            repository.getFavorites()
                .catch {
                    _event.emit(Event.ShowMessageToast(it.message.orEmpty()))
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
        isWeatherLoading = false,
        isForecastLoading = false,
        groupedForecasts = listOf(),
        selectedForecastGroupItems = listOf(),
        favorites = listOf()
    )

    override fun onEvent(event: Event) {
        viewModelScope.launch(dispatcherIo) {
            when (event) {
                is Event.ShowNetworkError -> _event.emit(Event.ShowNetworkError(event.message))
                is Event.ShowMessageToast -> _event.emit(Event.ShowMessageToast(event.message))
            }
        }
    }

    data class State(
        val data: HomeWeatherScreenData,
        val isFavorite: Boolean,
        val isWeatherLoading: Boolean,
        val isForecastLoading: Boolean,
        val groupedForecasts: List<HomeGroupedForecastData>,
        val selectedForecastGroupItems: List<ForecastItemData>,
        val favorites: List<FavoriteWeatherUiModel>
    )

    sealed class Event {
        class ShowNetworkError(val message: String = "") : Event()
        class ShowMessageToast(val message: String = "") : Event()
    }

}