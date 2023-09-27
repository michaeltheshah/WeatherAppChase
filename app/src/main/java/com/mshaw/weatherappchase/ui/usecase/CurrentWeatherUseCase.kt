package com.mshaw.weatherappchase.ui.usecase

import com.mshaw.data.util.state.toState
import com.mshaw.data.model.CurrentWeatherResponse
import com.mshaw.data.model.ReverseGeocodingResponse
import com.mshaw.data.repository.CurrentWeatherRepository
import com.mshaw.data.util.extensions.awaitResult
import com.mshaw.data.util.state.State
import javax.inject.Inject

// Typically, this would be in its own domain module, but for the sake of time,
// it's been placed in the UI module
class CurrentWeatherUseCase @Inject constructor(private val repositories: CurrentWeatherRepository) {
    suspend fun getCurrentWeatherByCityInput(
        searchQuery: String,
        units: String = "imperial"
    ): State<CurrentWeatherResponse> =
        repositories.fetchWeatherByCity(searchQuery, units).awaitResult().toState()

    suspend fun reverseGeocoding(
        latitude: Double,
        longitude: Double,
        units: String = "imperial"
    ): State<List<ReverseGeocodingResponse>> =
        repositories.reverseGeocoding(latitude, longitude, units).awaitResult().toState()
}