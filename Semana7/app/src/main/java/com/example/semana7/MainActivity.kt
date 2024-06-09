package com.example.semana7

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var mMapView: MapView
    private lateinit var shapeFileFeatureLayer: FeatureLayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mMapView = findViewById(R.id.mapView)
        setupMapHidrografia()
    }

    private fun setupMapHidrografia() {
        ArcGISRuntimeEnvironment.setApiKey("AAPKcf5e94f1e377405694368290e53b8227kqQGVvqyCFkJfDEU-kf_A8z5Zu_bg4ZobfZ_Qb4Oli7do106Tnn1TAewttRbwlWC")

        val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)
        mMapView.map = map


        mMapView.setViewpoint(Viewpoint(-20.0, -41.0, 2500000.0))

        val shapeFileFeatureTable = ServiceFeatureTable("https://services1.arcgis.com/vCWqzZcP1Hhe9FoD/arcgis/rest/services/doce/FeatureServer/0")

        shapeFileFeatureLayer = FeatureLayer(shapeFileFeatureTable)
        mMapView.map.operationalLayers.add(shapeFileFeatureLayer)
    }
}