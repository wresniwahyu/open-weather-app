package com.weather.app.screen.home

import com.weather.app.data.WeatherRepository
import com.weather.app.data.model.FavoriteWeatherUiModel
import com.weather.app.data.model.ForecastCityUiModel
import com.weather.app.data.model.ForecastItemUiModel
import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherCoordUiModel
import com.weather.app.data.model.WeatherDetailUiModel
import com.weather.app.data.model.WeatherMainUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.data.model.WeatherWindUiModel
import com.weather.app.ui.component.ForecastItemData
import com.weather.app.util.ApiError
import com.weather.app.util.ApiSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeWeatherViewModelTest {
    private val mockRepository = mockk<WeatherRepository>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()

    private val mockWeatherDetailUiModel = WeatherDetailUiModel(
        id = 123,
        main = "rain",
        description = "mostly rain",
        icon = "10d"
    )

    private val mockWeatherMainUiModel = WeatherMainUiModel(
        temp = 2.90,
        tempMax = 3.90,
        tempMin = 1.90,
        humidity = 90
    )

    private val mockWeatherWindUiModel = WeatherWindUiModel(
        speed = 10.0,
        deg = 3,
        gust = 3.0
    )

    private val mockWeatherUiModel = WeatherUiModel(
        id = 123456,
        dt = 123123123,
        name = "Yogyakarta",
        coord = WeatherCoordUiModel(0.0, 0.0),
        weatherDetail = mockWeatherDetailUiModel,
        main = mockWeatherMainUiModel,
        wind = mockWeatherWindUiModel
    )

    private val mockForecastUiModel = ForecastUiModel(
        city = ForecastCityUiModel(id = 123456, "Yogyakarta"),
        list = listOf(
            ForecastItemUiModel(
                dt = 10001,
                dtTxt = "2023-10-31 00:00",
                weather = mockWeatherDetailUiModel,
                main = mockWeatherMainUiModel,
                wind = mockWeatherWindUiModel
            ),
            ForecastItemUiModel(
                dt = 10002,
                dtTxt = "2023-11-1 00:00",
                weather = mockWeatherDetailUiModel,
                main = mockWeatherMainUiModel,
                wind = mockWeatherWindUiModel
            )
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `searchByName should trigger getWeatherData and getForecastData`() = runTest {
        val cityName = "TestCity"
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.getWeatherData(cityName) } coAnswers {
            ApiSuccess(
                mockWeatherUiModel
            )
        }
        coEvery { mockRepository.getForecastData(cityName) } coAnswers {
            ApiSuccess(
                mockForecastUiModel
            )
        }

        viewModel.searchByName(cityName)

        coVerify(exactly = 0) { mockRepository.getWeatherData(cityName) }
        coVerify(exactly = 0) { mockRepository.getForecastData(cityName) }

        delay(1000)

        coVerify(exactly = 1) { mockRepository.getWeatherData(cityName) }
        coVerify(exactly = 1) { mockRepository.getForecastData(cityName) }
    }

    @Test
    fun `getWeatherData should emit success state`() = runTest {
        val cityName = "TestCity"
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.getWeatherData(cityName) } coAnswers {
            ApiSuccess(
                mockWeatherUiModel
            )
        }
        coEvery { mockRepository.getFavorites() } coAnswers { flowOf() }

        viewModel.getWeatherData(cityName)

        coVerify { mockRepository.getWeatherData(cityName) }
        coVerify { mockRepository.getFavorites() }

        val state = viewModel.state.value
        val expectedStateData = HomeWeatherScreenData(
            id = mockWeatherUiModel.id,
            cityName = mockWeatherUiModel.name,
            temp = mockWeatherUiModel.main.temp,
            tempMin = mockWeatherUiModel.main.tempMin,
            tempMax = mockWeatherUiModel.main.tempMax,
            weatherDescription = mockWeatherUiModel.weatherDetail.description,
            humidity = mockWeatherUiModel.main.humidity,
            wind = mockWeatherUiModel.wind.speed
        )

        Assert.assertEquals(expectedStateData, state.data)
        Assert.assertTrue(!state.isWeatherLoading)
    }

    @Test
    fun `getWeatherData should emit error state`() = runTest {
        val cityName = "TestCity"
        val errorMessage = "Network error"
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.getWeatherData(cityName) } coAnswers { ApiError(message = errorMessage) }

        viewModel.getWeatherData(cityName)

        coVerify { mockRepository.getWeatherData(cityName) }

        val state = viewModel.state.value
        val defaultStateData = HomeWeatherScreenData(
            id = 0,
            cityName = "",
            temp = 0.0,
            tempMin = 0.0,
            tempMax = 0.0,
            weatherDescription = "",
            humidity = 0,
            wind = 0.0
        )

        Assert.assertEquals(defaultStateData, state.data)
        Assert.assertTrue(!state.isWeatherLoading)
    }

    @Test
    fun `getForecastData should emit success state data`() = runTest {
        val cityName = "TestCity"
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.getForecastData(cityName) } coAnswers {
            ApiSuccess(
                mockForecastUiModel
            )
        }

        viewModel.getForecastData(cityName)

        coVerify { mockRepository.getForecastData(cityName) }

        val state = viewModel.state.value
        val expectedStateData = mockForecastUiModel.getForecastGroupedByDate()

        Assert.assertEquals(expectedStateData, state.groupedForecasts)
        Assert.assertTrue(!state.isForecastLoading)
    }

    @Test
    fun `getForecastData should emit error state`() = runTest {
        val cityName = "TestCity"
        val errorMessage = "Network error"
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.getForecastData(cityName) } coAnswers { ApiError(message = errorMessage) }

        viewModel.getForecastData(cityName)

        coVerify { mockRepository.getForecastData(cityName) }

        val state = viewModel.state.value

        Assert.assertEquals(listOf<HomeGroupedForecastData>(), state.groupedForecasts)
        Assert.assertTrue(!state.isForecastLoading)
    }

    @Test
    fun `selectForecastItem should update selectedForecastGroupItems`() {
        val items = listOf(ForecastItemData("2023-11-1", 91, 23.0, 1.90, "10d"))
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        viewModel.selectForecastItem(items)

        val state = viewModel.state.value
        Assert.assertEquals(items, state.selectedForecastGroupItems)
    }

    @Test
    fun `addToFavorite should add or remove from favorites`() = runTest {
        val id = 1
        val name = "TestCity"
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.insertFavorite(id, name) } returns Unit
        coEvery { mockRepository.deleteFavorite(id, name) } returns Unit
        coEvery { mockRepository.getFavorites() } coAnswers { flowOf() }

        viewModel.addToFavorite(id, name)

        val state = viewModel.state.value
        if (state.isFavorite.not()) {
            coVerify { mockRepository.insertFavorite(id, name) }
        } else {
            coVerify { mockRepository.deleteFavorite(id, name) }
        }

        coVerify { mockRepository.getFavorites() }
    }

    @Test
    fun `getFavorites should update state with favorites`() = runTest {
        val favorites = listOf(
            FavoriteWeatherUiModel(1, "Jakarta"),
            FavoriteWeatherUiModel(2, "Yogyakarta")
        )
        val viewModel = HomeWeatherViewModel(testDispatcher, mockRepository)

        coEvery { mockRepository.getFavorites() } coAnswers { flowOf(favorites) }

        viewModel.getFavorites()

        val state = viewModel.state.value
        Assert.assertEquals(favorites, state.favorites)
    }

}
