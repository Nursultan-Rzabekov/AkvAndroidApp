package com.example.akvandroidapp.ui.main.home


import android.os.Bundle
import android.view.*
import com.example.akvandroidapp.R
import com.example.akvandroidapp.util.Constants
import com.yandex.mapkit.MapKitFactory


class HomeFragment : BaseHomeFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        MapKitFactory.setApiKey(Constants.MAPKIT_API_KEY)
        MapKitFactory.initialize(context)

        return inflater.inflate(R.layout.fragment_zhilye_layout, container, false)
    }



}

















