package com.example.akvandroidapp.ui.main.search

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.util.Constants.Companion.MAPKIT_API_KEY
import com.example.akvandroidapp.util.TextImageProvider
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.activity_main.*

class MapActivity : BaseActivity(), ClusterListener, ClusterTapListener
{
    private lateinit var mapView: MapView
    private val cluster = arrayListOf(Point(60.0,15.0))

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)

        mapView = findViewById(R.id.mapview)

        sessionManager.locationItem.observe(this, androidx.lifecycle.Observer{ dataState ->
            for(i in dataState){
                Log.d(TAG,"qwerty  = ${dataState}")
                cluster.add(i)
            }
        })

        Handler().postDelayed({
            if(!cluster.isNullOrEmpty()){
                Log.d(TAG,"qwerty cluster  = ${cluster}")
                mapView.map.move(CameraPosition(cluster.first(), 10F, 0F, 0F))
                val imageProvider = ImageProvider.fromResource(this@MapActivity, R.drawable.pin)
                val clusterizedCollection = mapView.map.mapObjects.addClusterizedPlacemarkCollection(this)
                val points = ArrayList<Point>()
                for (i in cluster.indices) {
                    points.add(Point(cluster[i].latitude, cluster[i].longitude))
                }
                Log.d("wqe","qwerty point + ${points}")
                clusterizedCollection.addPlacemarks(points, imageProvider, IconStyle())
                clusterizedCollection.clusterPlacemarks(60.0, 15)
            }
        }, 1000)
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
}