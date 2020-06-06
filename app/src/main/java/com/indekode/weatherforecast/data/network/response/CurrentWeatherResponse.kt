package com.indekode.weatherforecast.data.network.response

import com.google.gson.annotations.SerializedName
import com.indekode.weatherforecast.data.db.entity.CurrentWeatherEntry
import com.indekode.weatherforecast.data.db.entity.WeatherLocation

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation,
    val request: Request
)