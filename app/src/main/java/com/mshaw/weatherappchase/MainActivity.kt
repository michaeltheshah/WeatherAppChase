package com.mshaw.weatherappchase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mshaw.weatherappchase.ui.theme.WeatherAppTheme
import com.mshaw.weatherappchase.ui.view.CurrentWeatherScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                CurrentWeatherScreen()
            }
        }
    }
}

