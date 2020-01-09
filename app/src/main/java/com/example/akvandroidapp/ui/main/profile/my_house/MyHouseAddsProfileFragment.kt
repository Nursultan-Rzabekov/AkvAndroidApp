package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.AddAdInfo
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.favorite.FavoriteListAdapter
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.search_part_layout.*
import javax.inject.Inject


class MyHouseAddsProfileFragment :
    BaseProfileFragment() ,
    MyHouseListAdapter.Interaction ,
    MyHouseListAdapter.InteractionCheck,
    SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: MyHouseListAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    var myHousePostList: MutableList<AddAdInfo> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")

        subscribeObservers()
        initRecyclerView()

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        fragment_my_adds_add_more_btn.setOnClickListener {
            navNextFragment()
        }
    }

    private fun subscribeObservers(){
        sessionManager.favoritePostListItem.observe(this, Observer{ dataState ->
            Log.d(TAG, "favorite: ${dataState}")

            recyclerAdapter.apply {
                Log.d(TAG, "favorite: ${dataState}")

                preloadGlideImages(
                    requestManager = requestManager,
                    list = dataState
                )
                submitList(
                    blogList = dataState,
                    isQueryExhausted = true
                )
            }
        })

//        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
//            if(dataState != null) {
//                handlePagination(dataState)
//                stateChangeListener.onDataStateChange(dataState)
//            }
//        })
//
//        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
//            if(viewState != null){
//                recyclerAdapter.apply {
//                    //Log.d(TAG, "Search results responses: ${viewState.blogFields.blogList}")
//                    fragement_explore_layout_id.visibility = View.GONE
//                    fragment_explore_active_layout_id.visibility = View.VISIBLE
//                    preloadGlideImages(
//                        requestManager = requestManager,
//                        list = viewState.myHouseFields.blogList
//                    )
//                    submitList(
//                        blogList = viewState.myHouseFields.blogList,
//                        isQueryExhausted = viewState.myHouseFields.isQueryExhausted
//                    )
//                }
//            }
//        })
    }

    private fun handlePagination(dataState: DataState<ProfileViewState>){

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

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileMyHouseAddsProfileFragment_to_profileAddTypeFragment)
    }

    private fun navNextDetailFragment(item: BlogPost){
        val bundle = bundleOf("item" to item)
        findNavController().navigate(R.id.action_profileMyHouseAddsProfileFragment_to_profileMyHouseDetailProfileFragment,bundle)
    }

    private fun initRecyclerView(){
        fragment_my_adds_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseAddsProfileFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = MyHouseListAdapter(requestManager,  this@MyHouseAddsProfileFragment,this@MyHouseAddsProfileFragment)

            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        //viewModel.setBlogPost(item)

        navNextDetailFragment(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragment_my_adds_recycler_view.adapter = null
    }

    override fun onRefresh() {
        swipe_refresh.isRefreshing = false
    }

    override fun onItemSelected(position: Int, item: BlogPost, boolean: Boolean) {
        recyclerAdapter.removeAt(position)
    }
}