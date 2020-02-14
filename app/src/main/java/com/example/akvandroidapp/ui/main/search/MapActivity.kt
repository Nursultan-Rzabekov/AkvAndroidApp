package com.example.akvandroidapp.ui.main.search

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.ResolveInfo
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.Constants.Companion.MAPKIT_API_KEY
import com.example.akvandroidapp.util.Helper
import com.example.akvandroidapp.util.TextImageProvider
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.map.*

class MapActivity : BaseLocationActivity(), ClusterListener, ClusterTapListener,MapObjectTapListener
{
    private lateinit var mapView: MapView
    private val cluster :ArrayList<Point> = arrayListOf()
    private var latitude:Double = 0.0
    private var longitude:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        locationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        providers = locationManager.getProviders(true)

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionsToRequest = findUnAskedPermissions(permissions)

        if (!isGPS && !isNetwork) {
            showSettingsAlert(5)
            getLastLocation(locationManager)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size > 0) {
                    requestPermissions(
                        permissionsToRequest.toTypedArray(),
                        Constants.ALL_PERMISSIONS_RESULT
                    )
                    canGetLocation = false
                }
            }
            getLocation()
        }

        mapView = findViewById(R.id.mapview)

        sessionManager.location.observe(this, androidx.lifecycle.Observer{ dataState ->
            latitude = dataState.latitude
            longitude =dataState.longitude
        })

        sessionManager.locationItem.observe(this, androidx.lifecycle.Observer{ dataState ->
            for(i in dataState){
                cluster.add(i)
            }
        })
        Log.d(TAG,"qwerty cluster = ${cluster}")

        Handler().postDelayed({
            if(!cluster.isNullOrEmpty()){
                Log.d(TAG,"qwerty cluster  = ${cluster}")
                mapView.map.move(CameraPosition(cluster.first(), 3F, 0F, 0F))
                val imageProvider = ImageProvider.fromResource(this@MapActivity, R.drawable.pin)
                val clusterizedCollection = mapView.map.mapObjects.addClusterizedPlacemarkCollection(this)
                val points = ArrayList<Point>()
                for (i in cluster.indices) {
                    points.add(Point(cluster[i].latitude, cluster[i].longitude))
                }
                Log.d("wqe","qwerty point + ${points}")
                clusterizedCollection.addPlacemarks(points, imageProvider, IconStyle())
                clusterizedCollection.addTapListener(this)
                clusterizedCollection.clusterPlacemarks(60.0, 15)
            }
        }, 1000)


        zoom_map.setOnClickListener {
            if(latitude!=0.0 && longitude!=0.0){
                mapView.map.move(
                    CameraPosition(Point(latitude,longitude), 17.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 1F),
                    null
                )
                val imageProvider = ImageProvider.fromResource(this@MapActivity, R.drawable.pin)
                val place = mapView.map.mapObjects.addPlacemark(Point(latitude,longitude), imageProvider, IconStyle())
            }
            else{
                Helper.toastOnTop(
                    this,
                    resources.getString(R.string.geoNotFound),
                    Toast.LENGTH_SHORT,
                    -200
                )
            }
        }
    }


    override fun displayProgressBar(bool: Boolean){
        if(bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun expandAppBar() {
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }


    override fun onClusterAdded(cluster: Cluster) {
        cluster.appearance.setIcon(
            TextImageProvider(this,cluster.size.toString())
        )
        cluster.addClusterTapListener(this)
    }

    override fun onClusterTap(cluster: Cluster): Boolean {
        Toast.makeText(
            applicationContext,
            String.format(getString(R.string.cluster_tap_message), cluster.size),
            Toast.LENGTH_SHORT
        ).show()
        return true
    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        val uri: Uri =
            Uri.parse("yandexmaps://maps.yandex.ru/?rtext=${latitude},${longitude}~${p1.latitude},${p1.longitude}&rtt=mt")
        var intent = Intent(Intent.ACTION_VIEW, uri)

        val activities: List<ResolveInfo> =
            packageManager.queryIntentActivities(intent, 0)
        val isIntentSafe = activities.isNotEmpty()
        if (isIntentSafe) {
            startActivity(intent)
        } else { // Открываем страницу приложения Яндекс.Карты в Google Play.
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("market://details?id=ru.yandex.yandexmaps")
            startActivity(intent)
        }

        return true
    }
}