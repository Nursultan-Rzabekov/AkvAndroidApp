package com.example.akvandroidapp.ui.main.messages


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.messages.adapter.RequestListAdapter
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setOrderQueryExhausted
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import handleIncomingOrdersListData
import kotlinx.android.synthetic.main.fragment_requests.*
import loadOrderFirstPage
import nextOrderPage
import javax.inject.Inject


class RequestFragment : BaseMessagesFragment(),
    RequestListAdapter.Interaction, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: RequestListAdapter

    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_request.setOnRefreshListener(this)


        viewModel.loadOrderFirstPage()


        initRecyclerView()
        subscribeObservers()


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
                if(viewState.ordersField.orders.isNotEmpty()){
                    recyclerAdapter.apply {
                        Log.d(TAG, "Search results responses: ${viewState.myChatFields.blogList}")

                        preloadGlideImages(
                            requestManager = requestManager,
                            list = viewState.ordersField.orders
                        )
                        submitList(
                            blogList = viewState.ordersField.orders,
                            isQueryExhausted = viewState.myChatFields.isQueryExhausted
                        )
                    }
                }
            }
        })
    }

    private fun handlePagination(dataState: DataState<MessagesViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingOrdersListData(it)
                }
            }
        }

        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){
                    event.getContentIfNotHandled()
                    viewModel.setOrderQueryExhausted(true)
                }
            }
        }
    }


    private fun initRecyclerView(){
        fragment_requests_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@RequestFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = RequestListAdapter(requestManager,  this@RequestFragment)
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "BlogFragment: attempting to load next page...")
                        viewModel.nextOrderPage()
                    }
                }
            })
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: HomeReservation) {
        //viewModel.setBlogPost(item)

//        val intent = Intent(context,MessagesDetailActivity::class.java)
//        startActivity(intent)
//        findNavController().navigate(R.id.action_RequestFragment_to_MessagesDetailFragmentt)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        fragment_requests_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_request.isRefreshing = false
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadOrderFirstPage().let {
            resetUI()
        }
    }

    private  fun resetUI(){
        fragment_requests_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_request.requestFocus()
    }

}

















