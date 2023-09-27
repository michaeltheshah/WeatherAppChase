package com.mshaw.data.di

import com.mshaw.data.repository.CurrentWeatherRepository
import com.mshaw.data.repository.CurrentWeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideCurrentWeatherRepo(impl : CurrentWeatherRepositoryImpl) : CurrentWeatherRepository
}