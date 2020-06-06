package com.indekode.weatherforecast.data.repository

import androidx.lifecycle.LiveData
import com.indekode.weatherforecast.data.db.entity.CurrentWeatherEntry
import com.indekode.weatherforecast.data.db.entity.WeatherLocation

interface ForecastRepository {
    suspend fun getCurrentWeather(metric: Boolean): LiveData<out CurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>

}