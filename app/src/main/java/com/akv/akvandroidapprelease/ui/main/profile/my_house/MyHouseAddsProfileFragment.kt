package com.akv.akvandroidapprelease.ui.main.profile.my_house


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.entity.BlogPost
import com.akv.akvandroidapprelease.session.AddAdInfo
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.main.profile.add_ad.AddAdMainActivity
import com.akv.akvandroidapprelease.ui.main.profile.my_house.adapters.MyHouseListAdapter
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseStateStateEvent
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseViewState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.setHouseId
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.setQueryExhausted
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.setResponseState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.setState
import com.akv.akvandroidapprelease.util.ErrorHandling
import com.akv.akvandroidapprelease.util.TopSpacingItemDecoration
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_my_adds.*
import kotlinx.android.synthetic.main.fragment_my_adds_layout.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class MyHouseAddsProfileFragment : BaseMyHouseFragment(),
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

        setToolbar()
        initRecyclerView()
        subscribeObservers()

        if(savedInstanceState == null){
            viewModel.loadFirstPage()
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
                Log.d(TAG, "Search results responses ${viewState.myHouseStateFields.response}")
                if (viewState.myHouseStateFields.response){
                    Log.d(TAG, "Search results responses state changed: ${viewState.myHouseFields.page}, ${viewState.myHouseFields.blogList.size}")
                    onBlogSearchOrFilter()
                    viewModel.setResponseState(false)
                }else {
                    if (viewState.myHouseFields.blogList.isNotEmpty()) {
                        recyclerAdapter.apply {

                            Log.d(
                                TAG,
                                "Search results responses: ${viewState.myHouseFields.page}, ${viewState.myHouseFields.blogList.size}"
                            )

                            preloadGlideImages(
                                requestManager = requestManager,
                                list = viewState.myHouseFields.blogList
                            )

                            if (viewState.myHouseFields.page != 1)
                                submitList(
                                    blogList = viewState.myHouseFields.blogList,
                                    isQueryExhausted = viewState.myHouseFields.isQueryExhausted
                                )
                            else
                                clearAndSubmitList(
                                    blogList = viewState.myHouseFields.blogList,
                                    isQueryExhausted = viewState.myHouseFields.isQueryExhausted
                                )
                        }
                    }
                }
            }
        })
    }

    private fun handlePagination(dataState: DataState<MyHouseViewState>){

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
        val intent = Intent(context, AddAdMainActivity::class.java)
        startActivity(intent)
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

            recyclerAdapter =
                MyHouseListAdapter(
                    requestManager,
                    this@MyHouseAddsProfileFragment
                )
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
                        viewModel.setStateEvent(MyHouseStateStateEvent.MyHouseStateEvent)
                    }
                }
            }
        }
        else{
            item.id.let {
                viewModel.setHouseId(it).let {
                    viewModel.setState(1).let {
                        viewModel.setStateEvent(MyHouseStateStateEvent.MyHouseStateEvent)
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

    private fun setToolbar(){
        fragment_my_adds_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_my_adds_toolbar.setNavigationOnClickListener{
            activity?.finish()
        }
    }
}