package com.indekode.weatherforecast.data.provider

import com.indekode.weatherforecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitProvider(): UnitSystem
}