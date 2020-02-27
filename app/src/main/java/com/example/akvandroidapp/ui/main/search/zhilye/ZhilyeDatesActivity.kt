package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ReviewsPageAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_review_page_layout.*
import kotlinx.android.synthetic.main.fragment_reviews_page.*
import kotlinx.android.synthetic.main.search_part_layout.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class ZhilyeDatesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_ad_available_dates)
        subscribeObservers()

    }

    override fun expandAppBar() {

    }


    private fun subscribeObservers(){

    }

    override fun displayProgressBar(bool: Boolean) {

    }

}