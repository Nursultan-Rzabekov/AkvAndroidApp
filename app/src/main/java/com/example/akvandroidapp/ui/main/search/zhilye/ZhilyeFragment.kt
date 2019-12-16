package com.example.akvandroidapp.ui.main.search.zhilye


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
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
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView
import technolifestyle.com.imageslider.pagetransformers.ZoomOutPageTransformer


class ZhilyeFragment : BaseSearchFragment() {

    private lateinit var flipperLayout: FlipperLayout

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

        flipperLayout = view.findViewById(R.id.flipper_layout)
        flipperLayout.addPageTransformer(false, ZoomOutPageTransformer())

        // Uncomment to add your custom scroll time (default is 3 seconds)
        // flipperLayout.setScrollTimeInSec(5);

        setLayout()

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

    private fun setLayout() {
        val url =
            arrayOf("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg",
                "https://picsum.photos/300",
                "https://i.pinimg.com/originals/18/40/72/184072abb72399c23ab635faaa0a94ad.jpg")

        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in url.indices) {
            val view = FlipperView(context!!)
            view.setDescription("Cool" + (i + 1))
                .setDescriptionBackgroundColor(Color.TRANSPARENT)
                .resetDescriptionTextView()
                .setImage(url[i]) { flipperImageView, image ->
                    Glide.with(this@ZhilyeFragment).load(image as String).into(flipperImageView)
                }
            view.setOnFlipperClickListener(object : FlipperView.OnFlipperClickListener {
                override fun onFlipperClick(flipperView: FlipperView) {
                }
            })
            flipperViewList.add(view)
        }

        flipperLayout.addFlipperViewList(flipperViewList)
        flipperLayout.removeCircleIndicator()
        flipperLayout.showCircleIndicator()
        val view = FlipperView(context!!)
        view.setDescription("This is Black Panther II from new Marvel Movies")
        view.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
        view.setImage(R.drawable.error) { imageView, image ->
            imageView.setImageDrawable(image as Drawable)
        }
        flipperLayout.addFlipperView(view)
    }
}