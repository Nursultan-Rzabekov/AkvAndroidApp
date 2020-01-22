package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ReviewsPageAdapter
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_review_page_layout.*
import kotlinx.android.synthetic.main.fragment_reviews_page.*


class ZhilyeReviewActivity : BaseActivity() {

    private var house_id: Int? = null
    private lateinit var reviewsAdapter: ReviewsPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_review_page_layout)

        setToolbar()

        val passedIntent = intent.extras
        val bundle = passedIntent?.getBundle("house_id")
        house_id = bundle?.getInt("house_id", -1)
        Log.e("ZhilyeReviewActivity id", "$house_id")


        initRecyclerView()

    }

    override fun expandAppBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayProgressBar(bool: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setToolbar(){
        header_reviews_page_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back)
        setSupportActionBar(header_reviews_page_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_reviews_page_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun initRecyclerView(){
        fragment_reviews_page_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ZhilyeReviewActivity)
            reviewsAdapter = ReviewsPageAdapter(
                requestManager = requestManager
            )
            adapter = reviewsAdapter
        }
    }

}