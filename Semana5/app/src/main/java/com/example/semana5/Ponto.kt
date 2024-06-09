package com.example.semana5

class Ponto {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var altitude: Double = 0.0

    constructor()

    constructor(latitude: Double, longitude: Double, altitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = altitude
    }

    fun imprimir(): String {
        return """
            -------------------------
            Latitude: $latitude.
            Longitude: $longitude.
            Altitude: $altitude.
            -------------------------
        """.trimIndent()
    }
}
