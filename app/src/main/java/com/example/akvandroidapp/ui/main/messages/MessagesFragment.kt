package com.example.akvandroidapp.ui.main.messages


import android.os.Bundle
import android.view.*
import com.example.akvandroidapp.R
import com.yandex.mapkit.MapKitFactory


class MessagesFragment : BaseMessagesFragment(){

    private val MAPKIT_API_KEY = "74ae21de-d88e-44d9-9a2f-e02de671f696"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        MapKitFactory.initialize(context)

        val layout =  inflater.inflate(R.layout.explore_map, container, false)


        return layout
    }
}

















