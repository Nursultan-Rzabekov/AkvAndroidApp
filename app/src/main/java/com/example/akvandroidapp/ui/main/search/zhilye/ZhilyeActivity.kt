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
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.ZhilyeDetail
import com.example.akvandroidapp.entity.ZhilyeDetailProperties
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.messages.chatkit.CustomLayoutMessagesActivity
import com.example.akvandroidapp.ui.main.search.dialogs.DateRangePickerDialog
import com.example.akvandroidapp.ui.main.search.dialogs.GuestCounterDialog
import com.example.akvandroidapp.ui.main.search.viewmodel.getHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ApartmentPropertiesAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ApartmentsReviewsPageAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.RecommendationsAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.EndSpacingItemDecoration
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import com.google.android.material.appbar.AppBarLayout
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import handleIncomingZhilyeData
import kotlinx.android.synthetic.main.fragment_zhilye.*
import kotlinx.android.synthetic.main.fragment_zhilye_layout.*
import kotlinx.android.synthetic.main.header_zhilye.*
import kotlinx.android.synthetic.main.map.*
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ZhilyeActivity : BaseActivity(), ApartmentsReviewsPageAdapter.ShowMoreReviewInteraction,
    DateRangePickerDialog.DatePickerDialogInteraction,
    GuestCounterDialog.GuestCounterDialogInteraction{

    private val TAGV = "Zhilye Activity"

    private lateinit var maPView: MapView
    private val TARGET_LOCATION = Point(59.945933, 30.320045)
    lateinit var flipperLayout : FlipperLayout

    //bundle
    private var houseRules: List<ZhilyeDetailProperties> = listOf()
    private var selectedDates: List<Date> = listOf()
    private lateinit var zhilyeDetail: ZhilyeDetail

    private var userEmail:String? = null
    private var userName:String? = null
    private var userPic:String? = null
    private var userId:Int? = null

    //Adapters
    private lateinit var facilitiesAdapter: ApartmentPropertiesAdapter
    private lateinit var nearsAdapter: ApartmentPropertiesAdapter
    private lateinit var recommendationsAdapter: RecommendationsAdapter
    private lateinit var reviewsAdapter: ApartmentsReviewsPageAdapter

    private var isFavouriteChecked = false
    private var isToolbarColapsed = false

    lateinit var stateChangeListener: DataStateChangeListener

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: ZhilyeViewModel

    private var houseId: Int? = null
    private var isCancelState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(Constants.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)
        setContentView(R.layout.fragment_zhilye_layout)

        Locale.setDefault(Locale.forLanguageTag("ru"))

        houseId = intent.getIntExtra("houseId",67)
        isCancelState = intent.getBooleanExtra("isCancelState", false)

        viewModel = ViewModelProvider(this, providerFactory).get(ZhilyeViewModel::class.java)
        stateChangeListener = this


        houseId?.let {
            viewModel.setHouseId(it).let {
                viewModel.setStateEvent(ZhilyeStateEvent.BlogZhilyeEvent())
            }
        }

        setToolbar()

        initStateOfHouse()
        setMapView()
        setFlipperLayout(arrayListOf())
        initRecyclerViews()

        subscribeObservers()

        fragment_zhilye_house_rules_card.setOnClickListener {
            navHouseRules()
        }

        fragment_zhile_book_btn.setOnClickListener {
            navBookReserv()
        }

        chat_target_email.setOnClickListener {
            navigateChatActivity()
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
                Log.d("yes","list Reservations +${viewState.zhilyeFields.zhilyeReservationsList}")

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

                zhilyeDetail = viewState.zhilyeFields.zhilyeDetail

                isFavouriteChecked = viewState.zhilyeFields.zhilyeDetail.is_favourite

                changeFavouriteMenuBtnDrawable(
                    toolbar_zhilye_header.menu?.findItem(R.id.favourite)
                )

                moveMapTo(Point(
                    viewState.zhilyeFields.zhilyeDetail.latitude,
                    viewState.zhilyeFields.zhilyeDetail.longitude
                ))

                userEmail = viewState.zhilyeFields.zhilyeUser.email
                userName = viewState.zhilyeFields.zhilyeUser.first_name
                userPic = viewState.zhilyeFields.zhilyeUser.userpic
                userId = viewState.zhilyeFields.zhilyeUser.id

                facilitiesAdapter.submitList(viewState.zhilyeFields.zhilyeDetailAccomadations)
                nearsAdapter.submitList(viewState.zhilyeFields.zhilyeDetailNearBuildings)
                recommendationsAdapter.submitList(viewState.zhilyeFields.blogListRecommendations)
                reviewsAdapter.submitList(viewState.zhilyeFields.zhilyeReviewsList)

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
//        val intent = Intent(this, ZhilyeBookActivity::class.java)
//        startActivity(intent)
        showDatePicker()
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
            reviewsAdapter = ApartmentsReviewsPageAdapter(
                requestManager = requestManager,
                showMoreReviewInteraction = this@ZhilyeActivity
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

    private fun navigateChatActivity(){

        val intent = Intent(this, CustomLayoutMessagesActivity::class.java)
        intent.putExtra("email",userEmail)
        intent.putExtra("name",userName)
        intent.putExtra("image",userPic)
        intent.putExtra("id",userId)
        startActivity(intent)
    }

    private fun changeFavouriteMenuBtnDrawable(item: MenuItem?){
        if (isFavouriteChecked) {
            //viewModel.setStateEvent(ZhilyeStateEvent.СreateFavoriteItemEvent())
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_liked)
        }
        else if (isToolbarColapsed)
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_like_dark)
        else {
            //viewModel.setStateEvent(ZhilyeStateEvent.DeleteFavoriteItemEvent())
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_like_white)
        }
    }

    private fun changeShareMenuBtnDrawable(item: MenuItem?){
        if (isToolbarColapsed)
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_share_dark)
        else
            item?.icon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_share_white)
    }

    private fun initStateOfHouse(){
        if (isCancelState){

        }else{

        }
    }

    private fun showDatePicker(){
        val datePickerDialog = DateRangePickerDialog(this, listOf(), this)
        datePickerDialog.show()
    }

    private fun showCounterPicker(){
        val guestCounterDialog = GuestCounterDialog(this, 0,0, this)
        guestCounterDialog.show()
    }

    override fun onShowMorePressed() {
        navReviews(viewModel.getHouseId())
    }

    override fun onCloseBtnListener() {}

    override fun onClearBtnListener() {}

    override fun onSaveBtnListener(dates: List<Date>) {
        selectedDates = dates.toList()
        Log.d(TAGV, "selected dates: $selectedDates")
        showCounterPicker()
    }

    override fun onCloseBtnListenerCounter() {}

    override fun onClearBtnListenerCounter() {}

    override fun onSaveBtnListenerCounter(adultsCount: Int, childrenCount: Int) {
        val datesInLong = ArrayList<DateUtils.DateBundle>()
        for (date in selectedDates)
            datesInLong.add(DateUtils.DateBundle(date))
        Log.d(TAGV, "selected dates: $datesInLong")

        val bundle = bundleOf(
            "house_id" to houseId,
            "adultsCounter" to adultsCount,
            "children" to childrenCount,
            "zhilyeDetail" to zhilyeDetail
        )
        bundle.putParcelableArrayList("datesList", datesInLong)
        val intent = Intent(this, ZhilyeBookActivity::class.java)
        intent.putExtra("booking", bundle)
        startActivity(intent)
    }
}