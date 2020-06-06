package com.indekode.weatherforecast.data.network

import androidx.lifecycle.LiveData
import com.indekode.weatherforecast.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        unit: String
    )
}