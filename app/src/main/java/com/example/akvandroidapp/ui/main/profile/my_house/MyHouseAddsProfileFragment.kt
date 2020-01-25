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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.AddAdInfo
import com.example.akvandroidapp.session.HouseUpdateData
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.favorite.FavoriteListAdapter
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.search.SearchListAdapter
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.ui.main.search.viewmodel.setState
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_my_adds.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.swipe_refresh
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.search_part_layout.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class MyHouseAddsProfileFragment : BaseProfileFragment(),
    MyHouseListAdapter.Interaction,
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

        swipe_refresh_1.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()

        if(savedInstanceState == null){
            viewModel.loadFirstPage()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        fragment_my_adds_add_more_btn.setOnClickListener {
            navNextFragment()
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
                if(viewState.myHouseFields.blogList.isNotEmpty()){
                    recyclerAdapter.apply {
                        Log.d(TAG, "Search results responses: ${viewState.myHouseFields.blogList}")


                        preloadGlideImages(
                            requestManager = requestManager,
                            list = viewState.myHouseFields.blogList
                        )
                        submitList(
                            blogList = viewState.myHouseFields.blogList,
                            isQueryExhausted = viewState.myHouseFields.isQueryExhausted
                        )
                    }
                }
            }
        })
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
        sessionManager.setHouseUpdateData(
            HouseUpdateData(
                -1,
                item.name,
                item.name,
                price = item.price,
                address = item.house_type.toString())
        )
        sessionManager.setHouseUpdateFacilityItem("Утюг", true)

        findNavController().navigate(R.id.action_profileMyHouseAddsProfileFragment_to_profileMyHouseDetailProfileFragment,bundle)
    }

    private fun initRecyclerView(){
        fragment_my_adds_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseAddsProfileFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = MyHouseListAdapter(requestManager,  this@MyHouseAddsProfileFragment)
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
        navNextDetailFragment(item)
    }

    override fun onStateSelected(position: Int, item: BlogPost, text: String) {
        if(text == resources.getString(R.string.activate)){
            item.id.let {
                viewModel.setHouseId(it).let {
                    viewModel.setState(0).let {
                        viewModel.setStateEvent(ProfileStateEvent.MyHouseStateEvent())
                    }
                }
            }
        }
        else{
            item.id.let {
                viewModel.setHouseId(it).let {
                    viewModel.setState(1).let {
                        viewModel.setStateEvent(ProfileStateEvent.MyHouseStateEvent())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragment_my_adds_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_refresh_1.isRefreshing = false
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
            recyclerAdapter.clearList()
        }
    }

    private  fun resetUI(){
        fragment_my_adds_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_adds.requestFocus()
    }
}