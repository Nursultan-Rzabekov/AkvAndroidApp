package com.example.akvandroidapp.ui.main.search.zhilye


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.util.Constants
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_zhilye.*
import kotlinx.android.synthetic.main.header_zhilye.*
import kotlinx.android.synthetic.main.map.*
import technolifestyle.com.imageslider.FlipperLayout
import technolifestyle.com.imageslider.FlipperView


class ZhilyeActivity : BaseActivity() {

    private lateinit var maPView: MapView
    private val TARGET_LOCATION = Point(59.945933, 30.320045)
    lateinit var flipperLayout : FlipperLayout

    private var isFavouriteChecked = false
    private var blogPost: BlogPost? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(Constants.MAPKIT_API_KEY)
        MapKitFactory.initialize(this)

        setContentView(R.layout.fragment_zhilye_layout)

        blogPost = intent.extras?.get("BlogPost") as BlogPost
        Log.e("ZHILYE ACTIVITY BLOG", "$blogPost")

        setBlogInfo()
        setToolbar()
        setFlipperLayout()
        setMapView()

        fragment_zhilye_house_rules_card.setOnClickListener {
            navHouseRules()
        }

        fragment_zhilye_reviews_lb.setOnClickListener {
            navReviews()
        }

        fragment_zhile_book_btn.setOnClickListener {
            navBookReserv()
        }

        toolbar_zhilye_header.setNavigationOnClickListener{
            finish()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun expandAppBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        maPView.onStart()
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

        flipperLayout.addFlipperViewList(flipperViewList)
    }

    private fun setBlogInfo() {
        if (blogPost != null){

        }else{
            Toast.makeText(applicationContext, "No such BlogPost", Toast.LENGTH_SHORT).show()
            finish()
        }
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