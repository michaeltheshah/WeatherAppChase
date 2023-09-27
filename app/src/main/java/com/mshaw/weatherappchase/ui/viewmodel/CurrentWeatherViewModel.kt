package com.mshaw.weatherappchase.ui.viewmodel

import android.location.Location
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.mshaw.data.model.CurrentWeatherResponse
import com.mshaw.data.PreferencesManager
import com.mshaw.data.util.state.State
import com.mshaw.weatherappchase.ui.usecase.CurrentWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val preferencesManager: PreferencesManager,
    private val locationClient: FusedLocationProviderClient,
) : ViewModel() {
    private lateinit var locationCallback: LocationCallback
    private var currentUserLocation: Location? by mutableStateOf(null)
    private var userLocationObservable: Job? = null

    var searchInput by mutableStateOf("")

    private val _weatherResultsState = MutableStateFlow<State<CurrentWeatherResponse>>(State.Loading)
    val weatherResultsState = _weatherResultsState.asStateFlow()

    fun fetchWeatherResultsByCityInput() {
        _weatherResultsState.value = State.Loading
        userLocationObservable?.cancel()
        userLocationObservable = viewModelScope.launch {
            _weatherResultsState.value = currentWeatherUseCase.getCurrentWeatherByCityInput(searchInput)
        }
        saveLastLocationEntered()
    }

    fun startUpdatingCurrentWeatherByLocation() {
        _weatherResultsState.value = State.Loading
        userLocationObservable?.cancel()
        userLocationObservable = viewModelScope.launch {
            observeUserLocationUpdates()
                .collect { location ->
                    currentUserLocation = location
                    val lat = location.latitude
                    val lng = location.longitude

                    searchInput = when (val cityResult = currentWeatherUseCase.reverseGeocoding(lat, lng)) {
                        is State.Success -> {
                            cityResult.value.firstOrNull()?.searchQuery ?: ""
                        }
                        else -> ""
                    }
                    _weatherResultsState.value = currentWeatherUseCase.getCurrentWeatherByCityInput(searchInput)
                }
        }
    }

    private suspend fun observeUserLocationUpdates(): Flow<Location> {
        val locationRequest: LocationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5.seconds.inWholeMilliseconds
            ).apply {
                setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                setWaitForAccurateLocation(true)
            }.build()

        return callbackFlow {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    if (location != null) {
                        trySend(location)
                    } else {
                        close(IllegalStateException("Location returned is null"))
                    }
                }
            }

            locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            awaitClose {
                locationClient.removeLocationUpdates(locationCallback)
                saveLastLocationEntered()
            }
        }
    }

    private fun saveLastLocationEntered() {
        preferencesManager["city"] = searchInput
    }

    fun getLastSavedLocationEntered(): String? {
        return preferencesManager["city"]
    }
}