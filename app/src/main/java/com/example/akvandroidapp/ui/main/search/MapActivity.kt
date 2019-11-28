package com.example.akvandroidapp.ui.main.search

import android.app.Activity
import android.os.Bundle
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

/**
 * This is a basic example that displays a map and sets camera focus on the target location.
 * Note: When working on your projects, remember to request the required permissions.
 */
class MapActivity : BaseActivity() {
    /**
     * Replace "your_api_key" with a valid developer key.
     * You can get it at the https://developer.tech.yandex.ru/ website.
     */
    private val MAPKIT_API_KEY = "921f9910-6b90-4ad6-afc1-c4fd6898e3c0"
    private val TARGET_LOCATION =
        Point(59.945933, 30.320045)
    private lateinit var mapView: MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Set the api key before calling initialize on MapKitFactory.
         * It is recommended to set api key in the Application.onCreate method,
         * but here we do it in each activity to make examples isolated.
         */
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        /**
         * Initialize the library to load required native libraries.
         * It is recommended to initialize the MapKit library in the Activity.onCreate method
         * Initializing in the Application.onCreate method may lead to extra calls and increased battery use.
         */
        MapKitFactory.initialize(this)
        // Now MapView can be created.
        setContentView(R.layout.map)
        super.onCreate(savedInstanceState)
        mapView = findViewById(R.id.mapview)
        // And to show what can be done with it, we move the camera to the center of Saint Petersburg.
        mapView.getMap().move(
            CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 5F),
            null
        )
    }

    override fun onStop() { // Activity onStop call must be passed to both MapView and MapKit instance.
        mapView!!.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun displayProgressBar(bool: Boolean) {
    }

    override fun expandAppBar() {
    }

    override fun onStart() { // Activity onStart call must be passed to both MapView and MapKit instance.
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }
}