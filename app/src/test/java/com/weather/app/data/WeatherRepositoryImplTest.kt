package com.weather.app.data

import com.weather.app.data.mapper.FavoriteWeatherEntityToUiMapper
import com.weather.app.data.mapper.ForecastDtoToUiMapper
import com.weather.app.data.mapper.WeatherDtoToUiMapper
import com.weather.app.data.model.ForecastCityUiModel
import com.weather.app.data.model.ForecastItemUiModel
import com.weather.app.data.model.ForecastUiModel
import com.weather.app.data.model.WeatherCoordUiModel
import com.weather.app.data.model.WeatherDetailUiModel
import com.weather.app.data.model.WeatherMainUiModel
import com.weather.app.data.model.WeatherUiModel
import com.weather.app.data.model.WeatherWindUiModel
import com.weather.app.data.source.local.dao.WeatherDao
import com.weather.app.data.source.local.entity.FavoriteWeatherEntity
import com.weather.app.data.source.remote.dto.ForecastCityDto
import com.weather.app.data.source.remote.dto.ForecastDto
import com.weather.app.data.source.remote.dto.ForecastItemDto
import com.weather.app.data.source.remote.dto.WeatherCoordDto
import com.weather.app.data.source.remote.dto.WeatherDetailDto
import com.weather.app.data.source.remote.dto.WeatherDto
import com.weather.app.data.source.remote.dto.WeatherMainDto
import com.weather.app.data.source.remote.dto.WeatherWindDto
import com.weather.app.data.source.remote.service.ApiService
import com.weather.app.util.ApiError
import com.weather.app.util.ApiSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class WeatherRepositoryImplTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val apiService = mockk<ApiService>(relaxed = true)
    private val dao = mockk<WeatherDao>(relaxed = true)
    private val weatherMapper = mockk<WeatherDtoToUiMapper>(relaxed = true)
    private val forecastMapper = mockk<ForecastDtoToUiMapper>(relaxed = true)
    private val favoriteMapper = mockk<FavoriteWeatherEntityToUiMapper>(relaxed = true)

    private val mockWeatherDetailDto = WeatherDetailDto(
        id = 123,
        main = "rain",
        description = "mostly rain",
        icon = "10d"
    )

    private val mockWeatherMainDto = WeatherMainDto(
        temp = 2.90,
        tempMax = 3.90,
        tempMin = 1.90,
        humidity = 90
    )

    private val mockWeatherWindDto = WeatherWindDto(
        speed = 10.0,
        deg = 3,
        gust = 3.0
    )

    private val mockWeatherDto = WeatherDto(
        id = 123456,
        dt = 123123123,
        name = "Yogyakarta",
        coord = WeatherCoordDto(0.0, 0.0),
        weatherDetail = listOf(mockWeatherDetailDto),
        main = mockWeatherMainDto,
        wind = mockWeatherWindDto
    )

    private val mockForecastDto = ForecastDto(
        city = ForecastCityDto(id = 123456, "Yogyakarta"),
        list = listOf(
            ForecastItemDto(
                dt = 10001,
                dtTxt = "2023-10-31 00:00",
                weather = listOf(mockWeatherDetailDto),
                main = mockWeatherMainDto,
                wind = mockWeatherWindDto
            ),
            ForecastItemDto(
                dt = 10002,
                dtTxt = "2023-11-1 00:00",
                weather = listOf(mockWeatherDetailDto),
                main = mockWeatherMainDto,
                wind = mockWeatherWindDto
            )
        )
    )

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

    private val mockWeatherResponse = Response.success(mockWeatherDto)
    private val mockForecastResponse = Response.success(mockForecastDto)

    private val mockFavoriteEntity = FavoriteWeatherEntity(10001, "Jakarta")
    private val mockFavorites = listOf(mockFavoriteEntity)

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
    fun `getWeatherData should return data when success`() = runTest {
        val cityName = "TestCity"
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        coEvery { apiService.getWeatherData(cityName) } coAnswers { mockWeatherResponse }
        every { weatherMapper.map(mockWeatherDto) } answers { mockWeatherUiModel }

        val result = repository.getWeatherData(cityName)

        coVerify { apiService.getWeatherData(cityName) }
        verify { weatherMapper.map(mockWeatherDto) }

        val resultValue = (result as ApiSuccess).data
        Assert.assertTrue(result is ApiSuccess)
        Assert.assertEquals(mockWeatherUiModel, resultValue)

    }

    @Test
    fun `getWeatherData should return error when get error response`() = runTest {
        val cityName = "TestCity"
        val errorMessage = "Network error"
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        coEvery { apiService.getWeatherData(cityName) } throws Exception(errorMessage)

        val result = repository.getWeatherData(cityName)

        coVerify { apiService.getWeatherData(cityName) }

        Assert.assertTrue(result is ApiError)
    }

    @Test
    fun `getForecastData should return data when success`() = runTest {
        val cityName = "TestCity"
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        coEvery { apiService.getForecastData(cityName) } coAnswers { mockForecastResponse }
        every { forecastMapper.map(mockForecastDto) } answers { mockForecastUiModel }

        val result = repository.getForecastData(cityName)

        coVerify { apiService.getForecastData(cityName) }
        verify { forecastMapper.map(mockForecastDto) }

        val resultValue = (result as ApiSuccess).data
        Assert.assertTrue(result is ApiSuccess)
        Assert.assertEquals(mockForecastUiModel, resultValue)

    }

    @Test
    fun `getForecastData should return error when get error response`() = runTest {
        // Arrange
        val cityName = "TestCity"
        val errorMessage = "Network error"
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        coEvery { apiService.getForecastData(cityName) } throws Exception(errorMessage)

        val result = repository.getForecastData(cityName)

        coVerify { apiService.getForecastData(cityName) }

        Assert.assertTrue(result is ApiError)
    }

    @Test
    fun `insertFavorite should insert favorite item`() = runTest {
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        coEvery { dao.insert(mockFavoriteEntity) } returns Unit

        repository.insertFavorite(mockFavoriteEntity.id, mockFavoriteEntity.name)

        coVerify(exactly = 1) { dao.insert(mockFavoriteEntity) }
    }

    @Test
    fun `deleteFavorite should delete favorite data into db`() = runTest {
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        coEvery { dao.delete(mockFavoriteEntity) } returns Unit

        repository.deleteFavorite(mockFavoriteEntity.id, mockFavoriteEntity.name)

        coVerify(exactly = 1) { dao.delete(mockFavoriteEntity) }

    }

    @Test
    fun `getFavorites should get favorite data into db`() = runTest {
        val repository = WeatherRepositoryImpl(
            testDispatcher,
            apiService,
            dao,
            weatherMapper,
            forecastMapper,
            favoriteMapper
        )

        every { dao.getFavorites() } answers { flowOf(mockFavorites) }

        val result = repository.getFavorites().toList()

        coVerify(exactly = 1) { dao.getFavorites() }

        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(1, result.size)

    }

}