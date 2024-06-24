package com.example.semana7

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.data.Feature
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Callout
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult
import com.esri.arcgisruntime.mapping.view.MapView

class MainActivity : AppCompatActivity() {
    private lateinit var mMapView: MapView
    private lateinit var shapeFileFeatureLayer: FeatureLayer
    private lateinit var mCallout: Callout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ArcGISRuntimeEnvironment.setApiKey("3NKHt6i2urmWtqOuugvr9RRy79GDS5cvoylFlWyqaJeDSdqGXQKl4hNdDxFV3g44Jq1rMLRjnJPz0XnJd69S0NLE4rwJBbvivXTxmOpksOvGSKy-Jh6IB0EL7igyvAa2")
        mMapView = findViewById(R.id.mapView)
        setupMapHidrografia()
        setupMapCallout()
    }

    private fun setupMapHidrografia() {
        val map = ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC)
        mMapView.map = map
        mMapView.setViewpoint(Viewpoint(-20.0, -41.0, 2500000.0))
        val shapeFileFeatureTable = ServiceFeatureTable("https://services5.arcgis.com/sEhV5RkcBCb5rmjO/arcgis/rest/services/doce/FeatureServer/0")
        shapeFileFeatureLayer = FeatureLayer(shapeFileFeatureTable)
        mMapView.map.operationalLayers.add(shapeFileFeatureLayer)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupMapCallout() {
        mCallout = mMapView.callout

        mMapView.onTouchListener = object : DefaultMapViewOnTouchListener(this, mMapView) {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (mCallout.isShowing) {
                    mCallout.dismiss()
                }

                val screenPoint = android.graphics.Point(Math.round(e.x), Math.round(e.y))
                val tolerance = 10.0
                val identifyLayerResultListenableFuture: ListenableFuture<IdentifyLayerResult> =
                    mMapView.identifyLayerAsync(shapeFileFeatureLayer, screenPoint, tolerance, false)
                identifyLayerResultListenableFuture.addDoneListener {
                    try {
                        val identifyLayerResult = identifyLayerResultListenableFuture.get()

                        val calloutContent = TextView(applicationContext)
                        calloutContent.setTextColor(Color.BLACK)
                        calloutContent.setSingleLine(false)
                        calloutContent.isVerticalScrollBarEnabled = true
                        calloutContent.scrollBarStyle = View.SCROLLBARS_INSIDE_INSET
                        calloutContent.movementMethod = ScrollingMovementMethod.getInstance()

                        for (element in identifyLayerResult.elements) {
                            val feature = element as Feature
                            val attr = feature.attributes

                            for ((key, value) in attr) {
                                calloutContent.append("$key | $value\n")
                            }

                            val envelope = feature.geometry.extent
                            mMapView.setViewpointGeometryAsync(envelope, 200.0)
                            mCallout.location = envelope.center
                            mCallout.content = calloutContent
                            mCallout.show()
                        }
                    } catch (ex: Exception) {
                        Log.e("IdentifyLayerError", "Error identifying layer", ex)
                    }
                }
                return super.onSingleTapConfirmed(e)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null && event.action == MotionEvent.ACTION_UP) {
            mMapView.performClick()
        }
        return super.onTouchEvent(event)
    }
}