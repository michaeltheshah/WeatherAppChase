package com.mshaw.data.repository

import com.mshaw.data.model.CurrentWeatherResponse
import com.mshaw.data.model.ReverseGeocodingResponse
import com.mshaw.data.network.WeatherService
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

interface CurrentWeatherRepository {
    suspend fun fetchWeatherByCity(
        searchQuery: String,
        units: String
    ): Response<CurrentWeatherResponse>

    suspend fun reverseGeocoding(
        latitude: Double,
        longitude: Double,
        units: String
    ): Response<List<ReverseGeocodingResponse>>
}

class CurrentWeatherRepositoryImpl @Inject constructor(private val service: WeatherService): CurrentWeatherRepository {
    override suspend fun fetchWeatherByCity(
        searchQuery: String,
        units: String
    ): Response<CurrentWeatherResponse> {
        return service.fetchWeatherByCity(searchQuery, units)
    }

    override suspend fun reverseGeocoding(
        latitude: Double,
        longitude: Double,
        units: String
    ): Response<List<ReverseGeocodingResponse>> {
        return service.reverseGeocoding(latitude, longitude, units)
    }
}