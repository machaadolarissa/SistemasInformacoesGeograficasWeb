package com.example.semana4

import android.location.Location
import android.location.LocationListener

class MinhaLocalizacaoListener : LocationListener {
    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }

    companion object {
        var latitude: Double = 0.0
        var longitude: Double = 0.0
    }
}