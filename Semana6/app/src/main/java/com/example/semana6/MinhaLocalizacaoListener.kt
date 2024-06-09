package com.example.semana6

import android.location.Location
import android.location.LocationListener

class MinhaLocalizacaoListener : LocationListener {
    var latitude: Double = 0.0
        private set

    var longitude: Double = 0.0
        private set

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }
}
