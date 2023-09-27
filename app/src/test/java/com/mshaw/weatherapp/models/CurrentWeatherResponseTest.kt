package com.mshaw.weatherapp.models

import com.mshaw.data.model.Coord
import com.mshaw.data.model.CurrentWeatherResponse
import junit.framework.TestCase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurrentWeatherResponseTest : TestCase() {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }
    private var currentWeatherResponse: CurrentWeatherResponse? = null

    @BeforeEach
    public override fun setUp() {
        super.setUp()
        currentWeatherResponse = try {
            val stream = javaClass.getResourceAsStream("/current_weather_success.json") ?: return
            json.decodeFromStream<CurrentWeatherResponse>(stream)
        } catch (e: Exception) {
            null
        }
    }

    @Test
    fun shouldReturnCoordinates() {
        assert(currentWeatherResponse?.coord == Coord(40.7143, -74.006))
    }

    @Test
    fun shouldHaveWeatherList() {
        assert(currentWeatherResponse?.weather?.isNotEmpty() == true)
    }
}