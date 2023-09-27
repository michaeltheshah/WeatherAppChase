package com.mshaw.weatherappchase.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mshaw.data.model.CurrentWeatherResponse
import com.mshaw.data.util.state.State
import com.mshaw.weatherappchase.ui.viewmodel.CurrentWeatherViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentWeatherScreen(
    viewModel: CurrentWeatherViewModel = hiltViewModel()
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    if (!locationPermissionState.allPermissionsGranted) {
        LaunchedEffect(Unit) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        val lastSavedLocation = viewModel.getLastSavedLocationEntered()
        if (lastSavedLocation?.isNotBlank() == true) {
            viewModel.searchInput = lastSavedLocation
        }

        viewModel.startUpdatingCurrentWeatherByLocation()
    }

    Column {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.searchInput, onValueChange = {
                viewModel.searchInput = it
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.fetchWeatherResultsByCityInput()
                }
            )
        )

        when (val state = viewModel.weatherResultsState.collectAsStateWithLifecycle().value) {
            is State.Loading -> {
                LoadingScreen()
            }
            is State.Error -> {
                ErrorDialog(message = state.exception.message) {}
            }

            is State.Success -> {
                CurrentWeatherSuccess(state.value)
            }
            else -> {}
        }
    }
}

@Composable
fun CurrentWeatherSuccess(currentWeather: CurrentWeatherResponse) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top) {

            Spacer(modifier = Modifier.height(16.dp))

            CurrentWeatherContent(currentWeather)
        }
    }
}

@Composable
fun CurrentWeatherContent(
    currentWeather: CurrentWeatherResponse
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val weather = currentWeather.weather?.firstOrNull()
        AsyncImage(
            contentDescription = "Weather icon",
            model = weather?.iconUrl,
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
        )
        Text(
            text = weather?.main ?: "Loading...",
            modifier = Modifier,
            style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colors.onBackground)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 32.0.sp,
                        fontWeight = FontWeight(700.0.toInt()),
                        fontStyle = FontStyle.Normal,
                    )
                ) {
                    append("${currentWeather.main?.temp?.roundToInt() ?: "..."} ")
                }
                withStyle(
                    style = SpanStyle(
                        baselineShift = BaselineShift.Superscript,
                        fontSize = 8.0.sp,
                        fontWeight = FontWeight(300.0.toInt()),
                        fontStyle = FontStyle.Normal,
                    )
                ) { // AnnotatedString.Builder
                    append(" o")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 32.0.sp,
                        fontWeight = FontWeight(300.0.toInt()),
                        fontStyle = FontStyle.Normal,
                    )
                ) {
                    append("F")
                }
            },
            modifier = Modifier,
            style = TextStyle(fontSize = 32.sp, color = MaterialTheme.colors.onBackground),
        )

        Row {
            SubItems(
                title = "Humidity",
                value = "${currentWeather.main?.humidity ?: ".."}%",
            )
            Spacer(modifier = Modifier.width(8.dp))
            SubItems(
                title = "Feels Like",
                value = (currentWeather.main?.feelsLike?.roundToInt() ?: "...").toString()
            )
        }
    }
}


@Composable
fun SubItems(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colors.onBackground)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colors.onBackground)
        )
    }
}