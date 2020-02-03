package com.example.akvandroidapp.ui.main.home


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.home.state.HomeViewState
import com.example.akvandroidapp.ui.main.messages.adapter.ChatListAdapter
import com.example.akvandroidapp.ui.main.search.viewmodel.getCount
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import com.yandex.mapkit.MapKitFactory
import handleIncomingReservationListData
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_saved_booking.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class HomeFragment : BaseHomeFragment(),
    HomeListAdapter.Interaction, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: HomeListAdapter

    //

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_booking, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_home.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()

        viewModel.loadFirstPage()
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer {dataState ->
            if (dataState != null){
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            if (viewState != null){
                Log.e("HOME FRAGMENT COUNT", "${viewState.homeReservationField.count}")
                Log.e("HOME FRAGMENT size", "${viewState.homeReservationField.reservationList.size}")

                val trips = viewModel.getCount()

                if (trips == 0){
                    recyclerAdapter.submitList(
                        blogList = viewState.homeReservationField.reservationList,
                        isQueryExhausted = viewState.homeReservationField.isQueryExhausted
                    )
                }else {
                    if (viewState.homeReservationField.reservationList.isNotEmpty()) {
                        val guests =
                            viewState.homeReservationField.reservationList.sumBy { it.guests!! }
                        listFilledState(trips, guests)
                        recyclerAdapter.apply {
                            preloadGlideImages(
                                requestManager = requestManager,
                                list = viewState.homeReservationField.reservationList
                            )
                            submitList(
                                blogList = viewState.homeReservationField.reservationList,
                                isQueryExhausted = viewState.homeReservationField.isQueryExhausted
                            )
                        }
                    }
                }
            }
        })
    }


    private fun initRecyclerView(){
        fragment_saved_booking_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = HomeListAdapter(requestManager,  this@HomeFragment)
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

    override fun onItemSelected(position: Int, item: HomeReservation) {
        val bundle = bundleOf("houseId" to item.house_id)
        findNavController().navigate(R.id.action_homeFragment_to_zhilyeFragment,bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        fragment_saved_booking_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_home.isRefreshing = false
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            resetUI()
        }
    }

    private fun handlePagination(dataState: DataState<HomeViewState>){
        dataState.data?.let {
            it.data?.let {
                it.getContentIfNotHandled()?.let {
                    viewModel.handleIncomingReservationListData(it)
                }
            }
        }

        dataState.error?.let {event ->
            event.peekContent().response.message?.let {
                if (ErrorHandling.isPaginationDone(it))

                // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()

                // set query exhausted to update RecyclerView with
                // "No more results..." list item
                viewModel.setQueryExhausted(true)
            }
        }
    }

    private fun listEmptyState(){
        fragment_saved_booking_trips_tv.text = "0 поездки"
        fragment_saved_booking_guets_tv.text = "0 гостей"
    }

    private fun listFilledState(trips: Int, guests: Int){
        fragment_saved_booking_trips_tv.text = ("$trips поездки")
        fragment_saved_booking_guets_tv.text = ("$guests гостей")
    }

    private  fun resetUI(){
        fragment_saved_booking_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_home.requestFocus()
    }

}