package com.example.semana6

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.view.LocationDisplay
import com.esri.arcgisruntime.mapping.view.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var mMapView: MapView
    private lateinit var mLocationDisplay: LocationDisplay
    private lateinit var minhaLocalizacaoListener: MinhaLocalizacaoListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMapView = findViewById(R.id.mapView)
        minhaLocalizacaoListener = MinhaLocalizacaoListener()
        setupMap()
        setupLocationDisplay()
        setupGPS()
    }

    private fun setupMap() {
        mMapView.let { mapView ->
            val basemapType = Basemap.Type.STREETS_VECTOR
            val latitude = minhaLocalizacaoListener.latitude
            val longitude = minhaLocalizacaoListener.longitude
            val levelOfDetail = 20
            val map = ArcGISMap(basemapType, latitude, longitude, levelOfDetail)
            mapView.map = map
        }
    }

    override fun onPause() {
        super.onPause()
        mMapView.pause()
    }

    override fun onResume() {
        super.onResume()
        mMapView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.dispose()
    }

    private fun setupLocationDisplay() {
        mLocationDisplay = mMapView.locationDisplay
        mLocationDisplay.autoPanMode = LocationDisplay.AutoPanMode.COMPASS_NAVIGATION
        mLocationDisplay.startAsync()
    }

    private fun setupGPS() {
        mLocationDisplay.addDataSourceStatusChangedListener { dataSourceStatusChangedEvent ->
            if (dataSourceStatusChangedEvent.isStarted || dataSourceStatusChangedEvent.error == null) {
                return@addDataSourceStatusChangedListener
            }
            val requestPermissionsCode = 2
            val requestPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (!(ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    requestPermissions[0]
                ) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            requestPermissions[1]
                        ) == PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity, requestPermissions,
                    requestPermissionsCode
                )
            } else {
                Toast.makeText(this@MainActivity, "Erro.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mLocationDisplay.startAsync()
        } else {
            Toast.makeText(this@MainActivity, "Permissão recusada", Toast.LENGTH_SHORT).show()
        }
    }
}
