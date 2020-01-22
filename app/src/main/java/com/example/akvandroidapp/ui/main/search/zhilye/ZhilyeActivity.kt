package com.example.akvandroidapp.ui.main.search.zhilye


import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.ZhilyeDetailProperties
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.search.viewmodel.getHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ApartmentPropertiesAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ApartmentReviewsAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.RecommendationsAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.EndSpacingItemDecoration
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import com.google.android.material.appbar.AppBarLayout
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import handleIncomingZhilyeData
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_zhilye.*
import kotlinx.android.synthetic.main.fragment_zhilye_layout.*
import kotlinx.android.synthetic.main.header_zhilye.*
import kotlinx.android.synthetic.main.map.*
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView
import javax.inject.Inject


class ZhilyeActivity : BaseActivity() {

    private val TAGV = "Zhilye Activity"

    private lateinit var maPView: MapView
    private val TARGET_LOCATION = Point(59.945933, 30.320045)
    lateinit var flipperLayout : FlipperLayout

    //bundle
    private var houseRules: List<ZhilyeDetailProperties> = listOf()

    //Adapters
    private lateinit var facilitiesAdapter: ApartmentPropertiesAdapter
    private lateinit var nearsAdapter: ApartmentPropertiesAdapter
    private lateinit var recommendationsAdapter: RecommendationsAdapter
    private lateinit var reviewsAdapter: ApartmentReviewsAdapter

    private var isFavouriteChecked = false
    private var isToolbarColapsed = false

    lateinit var stateChangeListener: DataStateChangeListener
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: ZhilyeViewModel

    private var houseId:Int?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(Constants.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        setContentView(R.layout.fragment_zhilye_layout)

        houseId = intent.getIntExtra("houseId",67)

        viewModel = ViewModelProvider(this, providerFactory).get(ZhilyeViewModel::class.java)
        stateChangeListener = this


        houseId?.let {
            viewModel.setHouseId(it).let {
                viewModel.setStateEvent(ZhilyeStateEvent.BlogZhilyeEvent())
            }
        }

        subscribeObservers()
        setToolbar()
        setMapView()
        setFlipperLayout(arrayListOf())
        initRecyclerViews()


        fragment_zhilye_house_rules_card.setOnClickListener {
            navHouseRules()
        }

        fragment_zhilye_reviews_lb.setOnClickListener {
            navReviews(viewModel.getHouseId())
        }


        fragment_zhile_book_btn.setOnClickListener {
            navBookReserv()
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer{ dataState ->
            if(dataState != null) {
                handleUpdate(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(this, Observer{ viewState ->
            if(viewState != null){
                Log.d("yes","list house +${viewState.zhilyeFields.houseId}")
                Log.d("yes","list zhilyeDetail +${viewState.zhilyeFields.zhilyeDetail}")
                Log.d("yes","list Recommendations +${viewState.zhilyeFields.blogListRecommendations}")
                Log.d("yes","list Accomadations +${viewState.zhilyeFields.zhilyeDetailAccomadations}")
                Log.d("yes","list Photos +${viewState.zhilyeFields.zhilyeDetailPhotos}")
                Log.d("yes","list DetailRules +${viewState.zhilyeFields.zhilyeDetailRules}")
                Log.d("yes","list User +${viewState.zhilyeFields.zhilyeUser}")
                Log.d("yes","list DetailNearBuildings +${viewState.zhilyeFields.zhilyeDetailNearBuildings}")

                fragment_zhilye_host_nickname_tv.text =
                    ("@${viewState.zhilyeFields.zhilyeUser.first_name}")
                requestManager
                    .load(viewState.zhilyeFields.zhilyeUser.userpic)
                    .error(R.drawable.profile_default_avavatar)
                    .into(fragment_zhilye_host_image_civ)
                fragment_zhilye_hotel_name_tv.text =
                    viewState.zhilyeFields.zhilyeDetail.name
                fragment_zhilye_hotel_location_tv.text =
                    viewState.zhilyeFields.zhilyeDetail.city
                fragment_zhilye_hotel_description_tv.text =
                    viewState.zhilyeFields.zhilyeDetail.description
                fragment_zhilye_address_tv.text =
                    viewState.zhilyeFields.zhilyeDetail.address
                fragment_zhile_rating_tv.text =
                    viewState.zhilyeFields.zhilyeDetail.rating.toString()
                fragment_zhile_price_tv.text =
                    ("${viewState.zhilyeFields.zhilyeDetail.price}kzt/ночь")

                moveMapTo(Point(
                    viewState.zhilyeFields.zhilyeDetail.latitude,
                    viewState.zhilyeFields.zhilyeDetail.longitude
                ))

                facilitiesAdapter.submitList(viewState.zhilyeFields.zhilyeDetailAccomadations)
                nearsAdapter.submitList(viewState.zhilyeFields.zhilyeDetailNearBuildings)
                recommendationsAdapter.submitList(viewState.zhilyeFields.blogListRecommendations)
                reviewsAdapter.submitList(listOf())

                val photos: ArrayList<String> = arrayListOf()
                for(photo in viewState.zhilyeFields.zhilyeDetailPhotos)
                    photos.add(photo.image!!)
                setFlipperLayout(photos)

                houseRules = viewState.zhilyeFields.zhilyeDetailRules
            }
        })
    }

    private fun handleUpdate(dataState: DataState<ZhilyeViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingZhilyeData(it)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{

            }
        }
    }


    private fun navHouseRules(){
        val bundle = bundleOf(
            "houseRules" to houseRules
        )
        val intent = Intent(this, ZhilyeRulesOfHouseActivity::class.java)
        intent.putExtra("houseRules", bundle)
        startActivity(intent)
    }

    private fun navReviews(house_id: Int){
        val bundle = bundleOf(
            "house_id" to house_id
        )
        val intent = Intent(this, ZhilyeReviewActivity::class.java)
        intent.putExtra("house_id", bundle)
        startActivity(intent)
    }

    private fun navBookReserv(){
        val intent = Intent(this, ZhilyeBookActivity::class.java)
        startActivity(intent)
    }

    override fun onStop() {
        maPView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


    override fun displayProgressBar(bool: Boolean) {
        if(bool){
            progress_bar_zhilye.visibility = View.VISIBLE
        }
        else{
            progress_bar_zhilye.visibility = View.GONE
        }
    }

    override fun expandAppBar() {

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        maPView.onStart()
    }

    private fun initRecyclerViews(){
        fragment_zhilye_udopstva_recycler_view.apply {
            layoutManager = GridLayoutManager(this@ZhilyeActivity, 2)
            facilitiesAdapter = ApartmentPropertiesAdapter()
            adapter = facilitiesAdapter
        }
        fragment_zhilye_near_recycler_view.apply {
            layoutManager = GridLayoutManager(this@ZhilyeActivity, 2)
            nearsAdapter = ApartmentPropertiesAdapter()
            adapter = nearsAdapter
        }
        fragment_zhilye_recommendations_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ZhilyeActivity, LinearLayoutManager.HORIZONTAL, false)
            val endSpacingItemDecoration = EndSpacingItemDecoration(EndSpacingItemDecoration.STANDARD_SPACING)
            removeItemDecoration(endSpacingItemDecoration) // does nothing if not applied already
            addItemDecoration(endSpacingItemDecoration)

            recommendationsAdapter = RecommendationsAdapter(
                requestManager = requestManager
            )
            adapter = recommendationsAdapter
        }
        fragment_zhilye_reviews_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ZhilyeActivity)
            reviewsAdapter = ApartmentReviewsAdapter(
                requestManager = requestManager
            )
            adapter = reviewsAdapter
        }
    }

    private fun setToolbar(){
        toolbar_zhilye_header.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_white)
        setSupportActionBar(toolbar_zhilye_header)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar_zhilye_header.setNavigationOnClickListener{
            finish()
        }

        fragment_zhilye_appbar.addOnOffsetChangedListener(
            object: AppBarLayout.OnOffsetChangedListener{
                var scrollRange = -1

                override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                    if (scrollRange == -1)
                        scrollRange = fragment_zhilye_appbar.totalScrollRange
                    if (scrollRange + verticalOffset == 0){
                        isToolbarColapsed = true
                        toolbar_zhilye_header.navigationIcon =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_back)
                    }else{
                        isToolbarColapsed = false
                        toolbar_zhilye_header.navigationIcon =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_white)
                    }
                    changeFavouriteMenuBtnDrawable(
                        toolbar_zhilye_header.menu?.findItem(R.id.favourite)
                    )
                    changeShareMenuBtnDrawable(
                        toolbar_zhilye_header.menu?.findItem(R.id.share)
                    )
                }
            }
        )
    }

    private fun setMapView(){
        maPView = mapview
        maPView.map.move(
            CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 4F),
            null
        )
    }

    private fun moveMapTo(point: Point){
        mapview.map.move(
            CameraPosition(point, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 4F),
            null
        )
    }

    private fun setFlipperLayout(urls: ArrayList<String>) {
        flipperLayout = findViewById(R.id.header_zhilye_flipper_layout)
        flipperLayout.removeAutoCycle()
        flipperLayout.showInnerPagerIndicator()
        flipperLayout.setIndicatorBackgroundColor(Color.TRANSPARENT)

        var photos: ArrayList<String> = arrayListOf("http://akv-technopark.herokuapp.com/media/userpics/_DSC0430.jpg")
        if (urls.size > 0)
            photos = urls

        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in photos.indices) {
            val view = FlipperView(this)
            view.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            view.setDescriptionBackgroundColor(Color.TRANSPARENT)
            view.setImage(photos[i]) { flipperImageView, image ->
                requestManager
                    .load(image)
                    .error(R.drawable.test_image_back)
                    .centerCrop()
                    .into(flipperImageView)
            }
            flipperViewList.add(view)
        }

        flipperLayout.removeAllFlipperViews()
        flipperLayout.addFlipperViewList(flipperViewList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_zhilye_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu?.findItem(R.id.favourite)
        item?.isChecked = isFavouriteChecked
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.favourite -> {
                isFavouriteChecked = !item.isChecked
                item.isChecked = isFavouriteChecked
                changeFavouriteMenuBtnDrawable(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeFavouriteMenuBtnDrawable(item: MenuItem?){
        if (isFavouriteChecked)
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_liked)
        else if (isToolbarColapsed)
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_like_dark)
        else
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_like_white)
    }

    private fun changeShareMenuBtnDrawable(item: MenuItem?){
        if (isToolbarColapsed)
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_share_dark)
        else
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_share_white)
    }
}