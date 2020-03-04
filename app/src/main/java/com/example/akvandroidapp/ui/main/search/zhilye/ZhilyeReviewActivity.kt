package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ReviewsPageAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_review_page_layout.*
import kotlinx.android.synthetic.main.fragment_reviews_page.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class ZhilyeReviewActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {

    private var house_id: Int? = null
    private lateinit var reviewsAdapter: ReviewsPageAdapter

    lateinit var stateChangeListener: DataStateChangeListener
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: ZhilyeReviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_review_page_layout)

        swipe_refresh_reviews.setOnRefreshListener(this)
        setToolbar()

        viewModel = ViewModelProvider(this, providerFactory).get(ZhilyeReviewViewModel::class.java)
        stateChangeListener = this


        val passedIntent = intent.extras
        val bundle = passedIntent?.getBundle("house_id")
        house_id = bundle?.getInt("house_id", -1)
        Log.e("ZhilyeReviewActivity id", "$house_id")

        initRecyclerView()
        subscribeObservers()

        house_id?.let {
            viewModel.setHouseId(it).let {
                viewModel.loadFirstPage()
            }
        }
    }


    private fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer{ dataState ->
            if(dataState != null) {
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(this, Observer{ viewState ->
            if(viewState != null){
                reviewsAdapter.apply {
                    Log.d(TAG, "Search results responses: ${viewState.reviewsField.reviewList}")
                    preloadGlideImages(
                        requestManager = requestManager,
                        list = viewState.reviewsField.reviewList
                    )
                    submitList(
                        items = viewState.reviewsField.reviewList,
                        isQueryExhausted = viewState.reviewsField.isQueryExhausted
                    )
                }
            }
        })
    }


    private fun handlePagination(dataState: DataState<ZhilyeReviewsViewState>){

        // Handle incoming data from DataState
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingBlogListData(it)
                }
            }
        }

        // Check for pagination end (no more results)
        // must do this b/c server will return an ApiErrorResponse if page is not valid,
        // -> meaning there is no more data.
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){

                    // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()

                    // set query exhausted to update RecyclerView with
                    // "No more results..." list item
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }

    override fun expandAppBar() {
        //ignore
    }


    override fun onDestroy() {
        super.onDestroy()
        fragment_reviews_page_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_refresh_reviews.isRefreshing = false
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
        }
    }

    private  fun resetUI(){
        fragment_reviews_page_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_reviews.requestFocus()
    }

    override fun displayProgressBar(bool: Boolean) {
        if(bool){
            progress_bar_reviews.visibility = View.VISIBLE
        }
        else{
            progress_bar_reviews.visibility = View.GONE
        }
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

            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == reviewsAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            })
            adapter = reviewsAdapter
        }
    }

}