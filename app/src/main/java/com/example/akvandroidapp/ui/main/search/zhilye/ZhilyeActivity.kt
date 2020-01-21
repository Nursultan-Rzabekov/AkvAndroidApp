package com.example.akvandroidapp.ui.main.search.zhilye


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ApartmentPropertiesAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
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

    //Adapters
    private lateinit var facilitiesAdapter: ApartmentPropertiesAdapter
    private lateinit var nearsAdapter: ApartmentPropertiesAdapter

    private var isFavouriteChecked = false

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
        setFlipperLayout()
        initRecyclerViews()


        fragment_zhilye_house_rules_card.setOnClickListener {
            navHouseRules()
        }

        fragment_zhilye_reviews_lb.setOnClickListener {
            navReviews()
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

                facilitiesAdapter.submitList(viewState.zhilyeFields.zhilyeDetailAccomadations)
                nearsAdapter.submitList(viewState.zhilyeFields.zhilyeDetailNearBuildings)
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
        val intent = Intent(this, ZhilyeRulesOfHouseActivity::class.java)
        startActivity(intent)
    }

    private fun navReviews(){
        val intent = Intent(this, ZhilyeReviewActivity::class.java)
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
    }

    private fun setToolbar(){
        toolbar_zhilye_header.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back_white)
        setSupportActionBar(toolbar_zhilye_header)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setMapView(){
        maPView = mapview
        maPView.map.move(
            CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 4F),
            null
        )
    }

    private fun setFlipperLayout() {
        flipperLayout = findViewById(R.id.header_zhilye_flipper_layout)
        flipperLayout.removeAutoCycle()
        flipperLayout.showInnerPagerIndicator()
        flipperLayout.setIndicatorBackgroundColor(Color.TRANSPARENT)

        val url =
            arrayOf("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg",
                "https://picsum.photos/300",
                "https://i.pinimg.com/originals/18/40/72/184072abb72399c23ab635faaa0a94ad.jpg")

        val flipperViewList: ArrayList<FlipperView> = ArrayList()
        for (i in url.indices) {
            val view = FlipperView(this)
            view.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            view.setDescriptionBackgroundColor(Color.TRANSPARENT)
            view.setImage(url[i]) { flipperImageView, image ->
                Glide.with(this@ZhilyeActivity).load(image).centerCrop().into(flipperImageView)
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

    private fun changeFavouriteMenuBtnDrawable(item: MenuItem){
        if (isFavouriteChecked)
            item.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_liked)
        else
            item.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_like_white)
    }
}