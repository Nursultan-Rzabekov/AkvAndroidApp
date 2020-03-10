package com.akv.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.util.Log
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.ui.BaseActivity
import com.akv.akvandroidapp.util.Constants.Companion.MAPKIT_API_KEY
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectDragListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapSetPlacemarkActivity : BaseActivity(), MapObjectDragListener
{
    private lateinit var mapView: MapView
    private var point : Point ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)
        mapView = findViewById(R.id.mapview)

        sessionManager.location.observe(this, androidx.lifecycle.Observer{ dataState ->
            val imageProvider = ImageProvider.fromResource(this@MapSetPlacemarkActivity, R.drawable.pin)
            val place = mapView.map.mapObjects.addPlacemark(Point(dataState.latitude,dataState.longitude), imageProvider, IconStyle())
            place.isDraggable = true
            place.setDragListener(this)
        })
    }
    override fun displayProgressBar(bool: Boolean){
//        if(bool){
//            progress_bar.visibility = View.VISIBLE
//        }
//        else{
//            progress_bar.visibility = View.GONE
//        }
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

    override fun onMapObjectDrag(p0: MapObject, p1: Point) {
        Log.e("mapTap  Long ", "mapTap  Long  ${p1.latitude} + and ${p1.longitude}")
        sessionManager.setAddAdHouseLocation(p1.latitude,p1.longitude)
    }

    override fun onMapObjectDragEnd(p0: MapObject) {
    }

    override fun onMapObjectDragStart(p0: MapObject) {
    }

}