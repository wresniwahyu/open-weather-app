package com.weather.app.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weather.app.R
import com.weather.app.ui.component.AppButton
import com.weather.app.ui.component.EmptyState
import com.weather.app.ui.component.FavoritesBs
import com.weather.app.ui.component.ForecastBs
import com.weather.app.ui.component.ForecastGroupItem
import com.weather.app.ui.component.LoadingState
import com.weather.app.ui.component.SearchBar
import com.weather.app.util.isNetworkAvailable
import com.weather.app.util.showToast

@Composable
fun HomeWeatherScreen(
    viewModel: HomeWeatherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var showNetworkNotAvailable by remember { mutableStateOf(false) }
    var showErrorState by remember { mutableStateOf(false) }
    var errorStateMessage by remember { mutableStateOf("") }
    var searchValue by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.event.collect { event ->
            when (event) {
                is HomeWeatherViewModel.Event.ShowNetworkError -> {
                    val message =
                        event.message.ifEmpty { context.getString(R.string.error_message) }
                    context.showToast(message)
                    errorStateMessage = message
                    showErrorState = true
                }

                is HomeWeatherViewModel.Event.ShowMessageToast -> {
                    val message =
                        event.message.ifEmpty { context.getString(R.string.error_message) }
                    context.showToast(message)
                }
            }
        }
    })

    Column {
        SearchBar(callback = {
            searchValue = it
            viewModel.searchByName(it)
            showNetworkNotAvailable = context.isNetworkAvailable().not()
        })

        if (showNetworkNotAvailable) {
            EmptyState(
                text = stringResource(R.string.message_network_unavailable),
                buttonText = stringResource(R.string.btn_try_again)
            ) {
                if (searchValue.isNotBlank()) {
                    viewModel.searchByName(searchValue)
                }
                showNetworkNotAvailable = false
            }
        } else {
            if (state.data.cityName.isNotBlank()) {
                HomeWeatherContent(viewModel = viewModel)
            } else {
                LoadingState(isLoading = state.isWeatherLoading)

                if (showErrorState) {
                    EmptyState(
                        text = errorStateMessage,
                        buttonText = stringResource(id = R.string.btn_try_again)
                    ) {
                        if (searchValue.isNotBlank()) {
                            viewModel.searchByName(searchValue)
                        }
                        showErrorState = false
                    }
                }

                if (!state.isWeatherLoading && showErrorState.not()) {
                    EmptyState(text = stringResource(R.string.message_search_city))
                }
            }
        }
    }
}

@Composable
fun HomeWeatherContent(
    modifier: Modifier = Modifier,
    viewModel: HomeWeatherViewModel
) {
    val state by viewModel.state.collectAsState()
    var showForecastBs by remember { mutableStateOf(false) }
    var showFavoritesBs by remember { mutableStateOf(false) }

    if (showForecastBs) {
        ForecastBs(
            items = state.selectedForecastGroupItems,
            onDismiss = {
                showForecastBs = false
            }
        )
    }

    if (showFavoritesBs) {
        FavoritesBs(
            items = state.favorites,
            onItemClicked = {
                viewModel.searchByName(it.name)
                showFavoritesBs = false
            },
            onDismiss = {
                showFavoritesBs = false
            })
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = state.data.cityName,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "${state.data.temp}˚C",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = state.data.weatherDescription,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "L: ${state.data.tempMin}˚C  H: ${state.data.tempMax}˚C",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Humidity: ${state.data.humidity}% | Wind: ${state.data.wind}m/s",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Spacer(modifier = Modifier.width(16.dp))
            AppButton(
                modifier = Modifier.weight(1f),
                buttonText = stringResource(
                    if (state.isFavorite) R.string.btn_saved else R.string.btn_save
                ),
                isOutlinedButton = state.isFavorite
            ) {
                viewModel.addToFavorite(state.data.id, state.data.cityName)
            }
            Spacer(modifier = Modifier.width(16.dp))
            AppButton(
                modifier = Modifier.weight(1f),
                buttonText = stringResource(R.string.btn_favorites),
                isOutlinedButton = false
            ) {
                showFavoritesBs = true
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))

        val grouped = state.groupedForecasts
        if (grouped.isEmpty()) {
            LoadingState(
                modifier = modifier.weight(1f),
                isLoading = state.isForecastLoading
            )
        } else {
            LazyColumn {
                items(state.groupedForecasts) {
                    ForecastGroupItem(
                        data = it.group,
                        onClick = {
                            viewModel.selectForecastItem(it.groupItems)
                            showForecastBs = true
                        }
                    )
                }
            }
        }

    }
}