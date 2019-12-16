package com.example.akvandroidapp.ui.main.messages


import android.os.Bundle
import android.view.*
import com.example.akvandroidapp.R
import com.example.akvandroidapp.util.Constants.Companion.MAPKIT_API_KEY
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.android.synthetic.main.map.*


class MessagesFragment : BaseMessagesFragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.just, container, false)

    }


}

















