package com.akv.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.entity.Review
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.BaseActivity
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.DataStateChangeListener
import com.akv.akvandroidapp.ui.main.search.dialogs.ReviewDialog
import com.akv.akvandroidapp.ui.main.search.viewmodel.*
import com.akv.akvandroidapp.ui.main.search.zhilye.adapters.ReviewsPageAdapter
import com.akv.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsStateEvent
import com.akv.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.akv.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import com.akv.akvandroidapp.util.ErrorHandling
import com.akv.akvandroidapp.viewmodels.ViewModelProviderFactory
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnListScrollListener
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_review_page_layout.*
import kotlinx.android.synthetic.main.fragment_reviews_page.*
import loadFirstPage
import nextPage
import javax.inject.Inject
import kotlin.math.roundToInt


class ZhilyeReviewActivity :
    BaseActivity(),
    SwipeRefreshLayout.OnRefreshListener,
    ReviewDialog.ReviewDialogInteraction,
    ReviewsPageAdapter.ReviewPageAdapterInteraction{

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

        fragment_reviews_fabtn.setOnClickListener {
            showReviewDialog()
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
                if (viewState.isReviewCreatedField || viewState.isReviewUpdatedField){
                    viewModel.setIsReviewCreated(false)
                    viewModel.setIsReviewUpdated(false)
                    onBlogSearchOrFilter()
                }
                else {
                    reviewsAdapter.apply {
                        Log.d(TAG, "Reviews results responses: ${viewState.reviewsField.reviewList}")
                        preloadGlideImages(
                            requestManager = requestManager,
                            list = viewState.reviewsField.reviewList
                        )
                        if (viewModel.getPage() != 1)
                            submitList(
                                items = viewState.reviewsField.reviewList
                            )
                        else
                            clearAndSubmitList(
                                items = viewState.reviewsField.reviewList
                            )
                    }
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
                sessionManager.accountProperties.value?.id,
                applicationContext,
                this@ZhilyeReviewActivity,
                requestManager
            )

            object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == reviewsAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            }

            adapter = reviewsAdapter
        }
    }

    private fun showReviewDialog(){
        val reviewdialog = ReviewDialog(this, 0,"", this)
        reviewdialog.show()
    }

    override fun onReviewCloseBtnListener() {
    }

    override fun onReviewSendBtnListener(stars: Float, body: String) {
        house_id?.let {
            viewModel.setStateEvent(ZhilyeReviewsStateEvent.CreateReviewEvent(
                body = body,
                stars = stars.roundToInt(),
                houseId = it
            ))
        }
    }

    override fun onReviewUpdateBtnListener(stars: Float, body: String, reviewId: Int) {
        house_id?.let {
            viewModel.setStateEvent(
                ZhilyeReviewsStateEvent.UpdateReviewEvent(
                    body = body,
                    houseId = it,
                    stars = stars.roundToInt(),
                    reviewId = reviewId
                )
            )
        }
    }

    override fun onDeleteMyReview() {
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
    }

    override fun onEditMyReview(review: Review) {
        val reviewdialog = ReviewDialog(
            this,
            review.stars?.roundToInt()?: 1,
            review.body.toString(),
            this,
            isUpdate = true
        )
        reviewdialog.reviewId = review.id
        reviewdialog.show()
    }

}