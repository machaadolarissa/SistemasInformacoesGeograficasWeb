package com.example.semana5

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat

class MainActivity : ComponentActivity() {
    private lateinit var PROVIDER: String
    private lateinit var p1: Ponto
    private lateinit var p2: Ponto
    private lateinit var mLocManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PROVIDER = LocationManager.GPS_PROVIDER
        mLocManager = getSystemService(LOCATION_SERVICE) as LocationManager
        p1 = Ponto()
        p2 = Ponto()
    }

    fun reset(v: View) {
        p1 = Ponto()
        p2 = Ponto()
        val edtPtoA = findViewById<EditText>(R.id.edtPtoA)
        edtPtoA.setText("")
        val edtPtoB = findViewById<EditText>(R.id.edtPtoB)
        edtPtoB.setText("")
    }

    fun verPontoA(v: View) {
        mostrarGoogleMaps(p1.latitude, p1.longitude)
    }

    fun verPontoB(v: View) {
        mostrarGoogleMaps(p2.latitude, p2.longitude)
    }

    fun lerPontoA(v: View) {
        p1 = getPonto(v)
        val edtPtoA = findViewById<EditText>(R.id.edtPtoA)
        edtPtoA.setText(p1.imprimir())
    }

    fun lerPontoB(v: View) {
        p2 = getPonto(v)
        val edtPtoB = findViewById<EditText>(R.id.edtPtoB)
        edtPtoB.setText(p2.imprimir())
    }

    private fun getPonto(v: View): Ponto {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }

        if (!mLocManager.isProviderEnabled(PROVIDER)) {
            Toast.makeText(this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show()
            throw IllegalStateException("GPS DESABILITADO")
        }

        val mLocListener = MinhaLocalizacaoListener()
        mLocManager.requestLocationUpdates(PROVIDER, 0, 0f, mLocListener)
        val localAtual = mLocManager.getLastKnownLocation(PROVIDER)

        return Ponto(localAtual!!.latitude, localAtual.longitude, localAtual.altitude)
    }


    fun calcularDistancia(v: View) {
        val mLocManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val resultado = FloatArray(1)
        Location.distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude, resultado)

        if (mLocManager.isProviderEnabled(PROVIDER)) {
            val texto = "*** Distância: ${resultado[0]}\n"
            Toast.makeText(this, texto, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun mostrarGoogleMaps(latitude: Double, longitude: Double) {
        val wv: WebView = findViewById(R.id.webv)
        wv.settings.javaScriptEnabled = true
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
    }
}