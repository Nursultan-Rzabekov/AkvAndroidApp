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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.viewmodel.removeDeletedBlogPost
import com.example.akvandroidapp.ui.main.search.viewmodel.setIsAuthorOfBlogPost
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.SuccessHandling.Companion.SUCCESS_BLOG_DELETED
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

    lateinit var flipperLayout : FlipperLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "SearchFragment: ${viewModel}")

        maPView = mapview
        maPView.map.move(
            CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 4F),
            null
        )

        flipperLayout = view.findViewById(R.id.header_zhilye_flipper_layout)
        flipperLayout.removeAutoCycle()
        flipperLayout.showInnerPagerIndicator()
        flipperLayout.setIndicatorBackgroundColor(Color.TRANSPARENT)

        subscribeObservers()


        fragment_zhilye_house_rules_card.setOnClickListener {
            navHouseRules()
        }

        fragment_zhilye_reviews_lb.setOnClickListener {
            navReviews()
        }

        fragment_zhile_book_btn.setOnClickListener {
            navBook()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
            stateChangeListener.onDataStateChange(dataState)

            if(dataState != null){
                dataState.data?.let { data ->
                    data.data?.getContentIfNotHandled()?.let { viewState ->
                        viewModel.setIsAuthorOfBlogPost(
                            viewState.viewBlogFields.isAuthorOfBlogPost
                        )
                    }
                    data.response?.peekContent()?.let{ response ->
                        if(response.message.equals(SUCCESS_BLOG_DELETED)){
                            viewModel.removeDeletedBlogPost()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.viewBlogFields.blogPost?.let{ blogPost ->
                setBlogProperties(blogPost)
            }

            if(viewState.viewBlogFields.isAuthorOfBlogPost){
                //adaptViewToAuthorMode()
            }
        })
    }

    private fun setBlogProperties(blogPost: BlogPost){
        fragment_zhilye_hotel_name_tv.text = blogPost.name
        fragment_zhile_price_tv.text = blogPost.price.toString()
        fragment_zhile_rating_tv.text = blogPost.rating.toString()
        fragment_zhilye_hotel_location_tv.text = blogPost.city

        val url =
            arrayOf(blogPost.image,
                "https://picsum.photos/300",
                "https://i.pinimg.com/originals/18/40/72/184072abb72399c23ab635faaa0a94ad.jpg")

        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in url.indices) {
            val view = FlipperView(requireContext())
            view.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            view.setDescriptionBackgroundColor(Color.TRANSPARENT)
            view.setImage(url[i]) { flipperImageView, image ->
                Glide.with(this@ZhilyeFragment).load(image).centerCrop().into(flipperImageView)
            }
            flipperViewList.add(view)
        }

        flipperLayout.addFlipperViewList(flipperViewList)
    }

    private fun navHouseRules(){
        findNavController().navigate(R.id.action_szhilyeFragment_to_zhilyeRulesOfHouseFragment)
    }

    private fun navReviews(){
        findNavController().navigate(R.id.action_szhilyeFragment_to_ZhilyeReviewFragment)
    }

    private fun navBook(){
        findNavController().navigate(R.id.action_szhilyeFragment_to_zhilyeBookFragment)
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