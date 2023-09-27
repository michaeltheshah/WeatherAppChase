package com.mshaw.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mshaw.data.BuildConfig
import com.mshaw.data.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun providesJson() = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun providesRetrofit(json: Json): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val url = chain
                    .request()
                    .url
                    .newBuilder()
                    .addQueryParameter("appid", BuildConfig.apiKey)
                    .build()
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
            .build()

        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): WeatherService =
        retrofit.create(WeatherService::class.java)
}