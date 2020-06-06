package com.indekode.weatherforecast.ui.weather

import androidx.lifecycle.ViewModel
import com.indekode.weatherforecast.data.provider.UnitProvider
import com.indekode.weatherforecast.data.repository.ForecastRepository
import com.indekode.weatherforecast.internal.UnitSystem
import com.indekode.weatherforecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitProvider()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
