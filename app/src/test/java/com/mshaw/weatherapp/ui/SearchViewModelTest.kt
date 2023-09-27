package com.mshaw.weatherapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import coil.request.Disposable
import com.google.android.gms.location.FusedLocationProviderClient
import com.mshaw.data.PreferencesManager
import com.mshaw.data.model.CurrentWeatherResponse
import com.mshaw.data.util.state.State
import com.mshaw.weatherappchase.ui.usecase.CurrentWeatherUseCase
import com.mshaw.weatherappchase.ui.viewmodel.CurrentWeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest : TestCase() {
    val testScheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(testScheduler)
    val testScope = TestScope(testDispatcher)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val currentWeatherUseCase = mockk<CurrentWeatherUseCase>()
    private val preferencesManager = mockk<PreferencesManager>()
    private val locationClient = mockk<FusedLocationProviderClient>()

    private val viewModel = CurrentWeatherViewModel(currentWeatherUseCase, preferencesManager, locationClient)

    private var successfulResponse: CurrentWeatherResponse? = null
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @BeforeEach
    public override fun setUp() {
        super.setUp()
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
        mockkConstructor(Disposable::class)
        mockkConstructor(State.Success::class)
        mockkConstructor(State.Error::class)

        successfulResponse = try {
            val stream = javaClass.getResourceAsStream("/current_weather_success.json") ?: return
            json.decodeFromStream<CurrentWeatherResponse>(stream)
        } catch (e: Exception) {
            null
        }
    }

    @Test
    fun shouldParseJsonAsResponse() = testScope.runTest {
        val successfulResponse = successfulResponse
        if (successfulResponse == null) {
            fail("successResponse is null")
        }

        assert(successfulResponse?.weather?.isNotEmpty() == true)
    }

    @Test
    fun shouldEmitSuccessState() = testScope.runTest {
        val successfulResponse = successfulResponse
        if (successfulResponse == null) {
            fail("successResponse is null")
            return@runTest
        }

        every { preferencesManager[any()] = any() } just Runs
        coEvery { currentWeatherUseCase.getCurrentWeatherByCityInput(any()) } returns State.Success(successfulResponse)
        viewModel.fetchWeatherResultsByCityInput()
        verify { viewModel.fetchWeatherResultsByCityInput() }
        advanceUntilIdle()
        verify { preferencesManager[any()] = any() }
        confirmVerified(State.Success(successfulResponse))
    }

    @Test
    fun shouldEmitErrorState() = testScope.runTest {
        val exception = Exception()

        every { preferencesManager[any()] = any() } just Runs
        coEvery { currentWeatherUseCase.getCurrentWeatherByCityInput(any()) } returns State.Error(exception)
        viewModel.fetchWeatherResultsByCityInput()
        verify { viewModel.fetchWeatherResultsByCityInput() }
        verify { preferencesManager[any()] = any() }
        confirmVerified(State.Error(exception))
    }

    @AfterEach
    public override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
    }
}