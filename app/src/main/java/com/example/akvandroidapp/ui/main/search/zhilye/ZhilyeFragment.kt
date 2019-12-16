package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.util.Constants
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_zhilye.*
import kotlinx.android.synthetic.main.map.*


class ZhilyeFragment : BaseSearchFragment() {

    private lateinit var maPView: MapView
    private val TARGET_LOCATION = Point(59.945933, 30.320045)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MapKitFactory.setApiKey(Constants.MAPKIT_API_KEY)
        MapKitFactory.initialize(context)
        return inflater.inflate(R.layout.fragment_zhilye_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "SearchFragment: ${viewModel}")

        maPView = mapview
        maPView.map.move(
            CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 4F),
            null
        )

        fragment_zhilye_house_rules_card.setOnClickListener {
            navHouseRules()
        }

        fragment_zhilye_reviews_lb.setOnClickListener {
            navReviews()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun navHouseRules(){
        findNavController().navigate(R.id.action_szhilyeFragment_to_zhilyeRulesOfHouseFragment)
    }

    private fun navReviews(){
        findNavController().navigate(R.id.action_szhilyeFragment_to_ZhilyeReviewFragment)
    }

    override fun onStop() {
        maPView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        maPView.onStart()
    }
}