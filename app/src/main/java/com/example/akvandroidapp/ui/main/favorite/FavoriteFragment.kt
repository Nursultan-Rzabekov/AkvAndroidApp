package com.example.akvandroidapp.ui.main.favorite


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteStateEvent
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteViewState
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.ui.main.search.viewmodel.setState
import com.example.akvandroidapp.util.ErrorHandling

import com.example.akvandroidapp.util.TopSpacingItemDecoration
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_saved_pages.*
import kotlinx.android.synthetic.main.fragment_saved_pages_empty.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.swipe_refresh
import loadFirstPage
import javax.inject.Inject


class FavoriteFragment : BaseFavoriteFragment(), FavoriteListAdapter.Interaction ,FavoriteListAdapter.InteractionCheck,
    SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: FavoriteListAdapter

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

        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()
        onBlogSearchOrFilter()

        fragment_saved_pages_empty_find_btn.setOnClickListener {
            fragment_saved_pages_empty_id.visibility = View.GONE
            fragment_saved_pages_filled_id.visibility = View.VISIBLE
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
            if(dataState != null) {
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
            if(viewState != null){
                Log.d(TAG, "favorite: ${viewState.blogFields.blogList}")

                fragment_saved_pages_empty_id.visibility = View.GONE
                fragment_saved_pages_filled_id.visibility = View.VISIBLE

                recyclerAdapter.apply {
                    Log.d(TAG, "favorite: ${viewState.blogFields.blogList}")

                    preloadGlideImages(
                        requestManager = requestManager,
                        list = viewState.blogFields.blogList
                    )
                    submitList(
                        blogList = viewState.blogFields.blogList,
                        isQueryExhausted = viewState.blogFields.isQueryExhausted
                    )
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
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = FavoriteListAdapter(requestManager,  this@FavoriteFragment,this@FavoriteFragment)

            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        val bundle = bundleOf("houseId" to item.id)
        findNavController().navigate(R.id.action_favoriteFragment_to_zhilyeFragment,bundle)
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
            viewModel.setStateEvent(FavoriteStateEvent.DeleteFavoriteItemEvent())
        }
    }
}














