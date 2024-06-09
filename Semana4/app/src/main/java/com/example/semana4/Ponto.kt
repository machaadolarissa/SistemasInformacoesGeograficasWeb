package com.example.semana4

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat

class Ponto : ComponentActivity() {
    private lateinit var PROVIDER: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PROVIDER = LocationManager.GPS_PROVIDER
    }

    fun reset(v: View) {
        p1 = Ponto()
        p2 = Ponto()
        var edtPtoA = findViewById<EditText>(R.id.edtPtoA)
        edtPtoA.setText("")
        var edtPtoB = findViewById<EditText>(R.id.edtPtoB)
        edtPtoB.setText("")
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun mostrarGoogleMaps(latitude: Double, longitude: Double) {
        val wv: WebView = findViewById(R.id.webv)
        wv.settings.javaScriptEnabled = true
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
    }

    private fun verPontoA(v: View) {
        mostrarGoogleMaps(p1.getLatitude(), p1.getLongitude())
    }

    private fun verPontoB(v: View) {
        mostrarGoogleMaps(p2.getLatitude(), p2.getLongitude())
    }

    private fun lerPontoA(v: View) {
        p1 = getPonto()
        val edtPtoA = findViewById<EditText>(R.id.edtPtoA)
        edtPtoA.setText(p1.imprimir2())
    }

    private fun lerPontoB(v: View) {
        p1 = getPonto()
        val edtPtoB = findViewById<EditText>(R.id.edtPtoB)
        edtPtoB.setText(p2.imprimir2())
    }

    fun getPonto() : Ponto {
        val mLocManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val mLocListener = MinhaLocalizacaoListener()

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
                    Manifest.permission.ACCESS_NETWORK_STATE
                ),
                1
            )
        }

        mLocManager.requestLocationUpdates(PROVIDER, 0, 0f, mLocListener)
        val localAtual = mLocManager.getLastKnownLocation(PROVIDER)
        if (!mLocManager.isProviderEnabled(PROVIDER)) {
            Toast.makeText(this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show()
        }
        return Ponto(localAtual!!.latitude, localAtual.longitude, localAtual.altitude)
    }

    fun calcularDistancia() {
        val mLocManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val mLocListener = MinhaLocalizacaoListener()

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
                    Manifest.permission.ACCESS_NETWORK_STATE
                ),
                1
            )
        }

        mLocManager.requestLocationUpdates(PROVIDER, 0, 0f, mLocListener)
        val localAtual = mLocManager.getLastKnownLocation(PROVIDER)
        if (!mLocManager.isProviderEnabled(PROVIDER)) {
            Toast.makeText(this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show()
        }
        return Ponto(localAtual!!.latitude, localAtual.longitude, localAtual.altitude)
    }
}