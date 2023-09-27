package com.mshaw.data.network

import com.mshaw.data.model.CurrentWeatherResponse
import com.mshaw.data.model.ReverseGeocodingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/data/2.5/weather")
    suspend fun fetchWeatherByCity(
        @Query("q") searchQuery: String,
        @Query("units") units: String,
    ): Response<CurrentWeatherResponse>

    @GET("/geo/1.0/reverse")
    suspend fun reverseGeocoding(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String = "imperial",
    ): Response<List<ReverseGeocodingResponse>>
}