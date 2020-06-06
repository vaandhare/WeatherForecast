package com.indekode.weatherforecast.data.repository

import androidx.lifecycle.LiveData
import com.indekode.weatherforecast.data.db.CurrentWeatherDao
import com.indekode.weatherforecast.data.db.WeatherLocationDao
import com.indekode.weatherforecast.data.db.entity.CurrentWeatherEntry
import com.indekode.weatherforecast.data.db.entity.WeatherLocation
import com.indekode.weatherforecast.data.network.WeatherNetworkDataSource
import com.indekode.weatherforecast.data.network.response.CurrentWeatherResponse
import com.indekode.weatherforecast.data.provider.LocationProvider
import com.indekode.weatherforecast.data.repository.ForecastRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    init {
        weatherNetworkDataSource.downloadCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData(metric)
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    private suspend fun initWeatherData(metric: Boolean) {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        //first time it is init or changed location
        if (lastWeatherLocation == null
            || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather(metric)
            return
        }

        if(isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
            fetchCurrentWeather(metric)
    }

    private suspend fun fetchCurrentWeather(metric: Boolean) {
        if(metric) weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString(), "m")
        else weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString(), "f")
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}