package com.akv.akvandroidapp.ui.main.home


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.entity.HomeReservation
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.main.home.state.HomeStateEvent
import com.akv.akvandroidapp.ui.main.home.state.HomeViewState
import com.akv.akvandroidapp.ui.main.profile.add_ad.AddAdMainActivity
import com.akv.akvandroidapp.ui.main.search.viewmodel.getCount
import com.akv.akvandroidapp.ui.main.search.viewmodel.setCancelState
import com.akv.akvandroidapp.ui.main.search.viewmodel.setPayState
import com.akv.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.akv.akvandroidapp.util.ErrorHandling
import com.akv.akvandroidapp.util.TopSpacingItemDecoration
import handleIncomingReservationListData
import kotlinx.android.synthetic.main.fragment_saved_booking.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class HomeFragment : BaseHomeFragment(),
    HomeListAdapter.Interaction, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: HomeListAdapter

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

    private fun navNextFragment(){
        val intent = Intent(context, AddAdMainActivity::class.java)
        startActivity(intent)
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
                if(viewState.cancelReservationField.isCancelled){
                    Log.d(TAG, "reserv canceled: page: ${viewState.homeReservationField.page}, ${viewState.homeReservationField.reservationList.size}")
                    onBlogSearchOrFilter()
                    viewModel.setCancelState(false)
                }
                if(viewState.payReservationField.isPayed){
                    Log.d(TAG, "reserv payed: page: ${viewState.payReservationField.reservationResponse}, ${viewState.homeReservationField.reservationList.size}")
                    Toast.makeText(context,"Yessssssss",Toast.LENGTH_SHORT).show()
                    onBlogSearchOrFilter()
                    viewModel.setPayState(false)
                }
//                if (trips == 0){
//                    recyclerAdapter.clearAndSubmitList(
//                        blogList = viewState.homeReservationField.reservationList,
//                        isQueryExhausted = viewState.homeReservationField.isQueryExhausted
//                    )
//                }
                else {
                    val guests =
                        viewState.homeReservationField.reservationList.sumBy { it.guests!! }
                    listFilledState(trips, guests)
                    recyclerAdapter.apply {
                        preloadGlideImages(
                            requestManager = requestManager,
                            list = viewState.homeReservationField.reservationList
                        )
                        if (viewState.homeReservationField.page != 1)
                            submitList(
                                blogList = viewState.homeReservationField.reservationList,
                                isQueryExhausted = viewState.homeReservationField.isQueryExhausted)
                        else
                            clearAndSubmitList(
                                blogList = viewState.homeReservationField.reservationList,
                                isQueryExhausted = viewState.homeReservationField.isQueryExhausted
                            )
                    }
                }
            }
        })

        sessionManager.payBox.observe(viewLifecycleOwner,Observer{
            if(it.id != 0 && !it.location.isNullOrBlank() && !it.payment_page_url.isNullOrBlank()){
                val bundle = bundleOf(
                    "url" to it.payment_page_url,
                    "item" to it
                )
                findNavController().navigate(R.id.action_homeFragment_to_payBoxPayWebViewFragment, bundle)
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
        item.status?.let {
            val bundle = bundleOf(
                "houseId" to item.house_id,
                "fromState" to item.status,
                "firstImage" to item.house_image
            )
            findNavController().navigate(R.id.action_homeFragment_to_zhilyeFragment, bundle)
        }
    }

    override fun onItemPay(position: Int, item: HomeReservation) {
        viewModel.setStateEvent(HomeStateEvent.HomePayReservationEvent(item.id))
    }

    override fun onItemCancelReserv(position: Int, item: HomeReservation) {
        viewModel.setStateEvent(HomeStateEvent.HomeCancelReservationEvent(item.id,"Не очень"))
    }

    override fun onBookMoreBtnPressed() {
//        val bundle = bundleOf(
//            "start" to "yes"
//        )
//        findNavController().navigate(R.id.action_homeFragment_to_searchFragment, bundle)
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