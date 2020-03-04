package com.example.akvandroidapp.ui.main.favorite


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteStateEvent
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setDeleteState
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.VerticalSpacingItemDecoration
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.*
import loadFirstPage
import nextPage
import java.util.*
import javax.inject.Inject


class FavoriteFragment : BaseFavoriteFragment(),
    FavoriteDifferListAdapter.Interaction ,
    FavoriteDifferListAdapter.InteractionCheck,
    FavoriteDifferListAdapter.InteractionAddMore,
    SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: FavoriteDifferListAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_pages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Locale.setDefault(Locale.forLanguageTag("ru"))

        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()
        onBlogSearchOrFilter()
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
            if(dataState != null) {
                Log.d(TAG, "favorites dataState changed")
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            if(viewState != null){
                if(viewState.deleteblogFields.isDeleted){
                    Log.d(TAG, "favorites deleted: page: ${viewState.blogFields.page}, ${viewState.blogFields.blogList.size}")
                    onBlogSearchOrFilter()
                    viewModel.setDeleteState(false)
                }
                else {
                    recyclerAdapter.apply {
                        Log.d(
                            TAG,
                            "favorites: page: ${viewState.blogFields.page}, ${viewState.blogFields.blogList.size}"
                        )
                        preloadGlideImages(
                            requestManager = requestManager,
                            list = viewState.blogFields.blogList
                        )

                        if (viewState.blogFields.page != 1)
                            submitList(
                                blogList = viewState.blogFields.blogList,
                                isQueryExhausted = viewState.blogFields.isQueryExhausted
                            )
                        else
                            clearAndSubmitList(
                                blogList = viewState.blogFields.blogList,
                                isQueryExhausted = viewState.blogFields.isQueryExhausted
                            )
                    }
                }
            }
        })
    }

    private fun handlePagination(dataState: DataState<FavoriteViewState>){

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

    private fun initRecyclerView(){
        fragment_saved_pages_filled_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
            val verticalSpacingDecorator = VerticalSpacingItemDecoration(8)
            removeItemDecoration(verticalSpacingDecorator) // does nothing if not applied already
            addItemDecoration(verticalSpacingDecorator)

            recyclerAdapter = FavoriteDifferListAdapter(
                requestManager,
                this@FavoriteFragment,
                this@FavoriteFragment,
                this@FavoriteFragment)

            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.nextPage()
                    }
                }
            })

            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        val bundle = bundleOf(
            "houseId" to item.id,
            "firstImage" to item.image
        )
        findNavController().navigate(R.id.action_favoriteFragment_to_zhilyeFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        fragment_saved_pages_filled_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_refresh.isRefreshing = false
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
            recyclerAdapter.clearList()
        }
    }

    private  fun resetUI(){
        fragment_saved_pages_filled_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_favorite.requestFocus()
    }

    override fun onItemSelected(position: Int, item: BlogPost, boolean: Boolean) {
        recyclerAdapter.removeAt(position)
        viewModel.setHouseId(item.id).let {
            viewModel.setStateEvent(FavoriteStateEvent.DeleteFavoriteItemEvent)
        }
    }

    override fun onAddMoreClicked() {
//        val bundle = bundleOf(
//            "start" to "yes"
//        )
//        findNavController().navigate(R.id.action_favoriteFragment_to_searchFragment, bundle)
    }

}














