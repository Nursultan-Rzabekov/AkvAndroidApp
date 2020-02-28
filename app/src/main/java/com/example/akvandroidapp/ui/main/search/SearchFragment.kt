package com.example.akvandroidapp.ui.main.search



import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
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
import com.example.akvandroidapp.ui.auth.dialogs.CodeValidationDialog
import com.example.akvandroidapp.ui.main.search.dialogs.DateRangePickerDialog
import com.example.akvandroidapp.ui.main.search.dialogs.GuestCounterDialog
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.*

import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.savvi.rangedatepicker.CalendarPickerView
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.dialog_filter_dates.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.header_searcher_base_layout.*
import kotlinx.android.synthetic.main.search_part_layout.*
import loadFirstPage
import nextPage
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SearchFragment :
    BaseSearchFragment(),
    SearchListAdapter.Interaction,
    SearchListAdapter.InteractionCheck,
    SwipeRefreshLayout.OnRefreshListener,
    DateRangePickerDialog.DatePickerDialogInteraction,
    GuestCounterDialog.GuestCounterDialogInteraction {

    private lateinit var recyclerAdapter: SearchListAdapter
    private var adultsCount = 0
    private var childrenCount = 0

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_part_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener(this)

        initSearchView()
        initRecyclerView()
        subscribeObservers()

        val start = arguments?.getString("start")
        start?.let{
            onBlogSearchOrFilter()
        }


        sessionManager.filterUpdateData.observe(viewLifecycleOwner, Observer {
            viewModel.apply {
                setCityQuery(it.setCityQuery)
                setBlogFilterTypeHouse(it.setBlogFilterTypeHouse)
                //setBlogFilterAccomadations(accomdationListString)
                setBlogFilterPrice(it.setBlogFilterPriceLeft,it.setBlogFilterPriceRight)
                setBlogFilterRooms(it.setBlogFilterRoomsLeft,it.setBlogFilterRoomsRight)
                setBlogFilterBeds(it.setBlogFilterBedsLeft,it.setBlogFilterBedsRight)
                setBlogOrdering(it.setBlogOrdering)
                setBlogVerified(it.setBlogVerified)
            }
            onBlogSearchOrFilter()
        })

        main_filter_img_btn.setOnClickListener {
            navFilter()
        }

        fragment_explore_apartments_iv.setOnClickListener {
            navApartments()
        }

        fragment_explore_homes_iv.setOnClickListener {

        }

        by_map_chip.setOnClickListener {
            navMapActivity()
        }

        by_guests_chip.setOnClickListener {
            showGuestDialog()
        }

        by_date_chip.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun navFilter(){
        findNavController().navigate(R.id.action_searchFragment_to_searchFilterFragment)
    }

    private fun navApartments(){
        findNavController().navigate(R.id.action_searchFragment_to_apartmentsFragment)
    }


    private fun navMapActivity(){
        findNavController().navigate(R.id.action_searchFragment_to_mapActivity)
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
//                if(viewState.blogFields.blogList.isNotEmpty()){
                    recyclerAdapter.apply {
                        //Log.d(TAG, "Search results responses: ${viewState.blogFields.blogList}")
                        fragement_explore_layout_id.visibility = View.GONE
                        fragment_explore_active_layout_id.visibility = View.VISIBLE
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

                    if (viewState.blogFields.dateStart == "" || viewState.blogFields.dateEnd == "")
                        whenChipEmpty(
                            by_date_chip,
                            getString(R.string.dates)
                        )
                    else
                        whenChipFiltered(
                            by_date_chip,
                            "${viewState.blogFields.dateStart}-${viewState.blogFields.dateEnd}"
                        )

                    if (viewState.blogFields.adultsCount + viewState.blogFields.childrenCount == 0)
                        whenChipEmpty(
                            by_guests_chip,
                            getString(R.string.guests)
                        )
                    else
                        whenChipFiltered(
                            by_guests_chip,
                            "${viewState.blogFields.adultsCount + viewState.blogFields.childrenCount} Гости"
                        )
//                }
            }
        })
    }

    private fun initSearchView(){

        main_search_et.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val searchQuery = v.text.toString()
                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
                viewModel.setQuery(searchQuery).let{
                    onBlogSearchOrFilter()
                }
            }
            true
        }

        main_search_img_btn.setOnClickListener {
            val searchQuery = main_search_et.text.toString()
            Log.e(TAG, "SearchView: (button) executing search...: ${searchQuery}")

            viewModel.setQuery(searchQuery).let {
                onBlogSearchOrFilter()
            }
        }
    }

    private fun handlePagination(dataState: DataState<SearchViewState>){

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
        blog_post_recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SearchFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = SearchListAdapter(requestManager,  this@SearchFragment,this@SearchFragment)
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
        val bundle = bundleOf("houseId" to item.id,
                                    "firstImage" to item.image)
        findNavController().navigate(R.id.action_searchFragment_to_zhilyeFragment,bundle)
    }

    override fun onItemSelected(position: Int, item: BlogPost, boolean: Boolean) {
        Log.d("favorite", "favorite search ${item} and ${boolean}")
        if(boolean){
            viewModel.setStateEvent(SearchStateEvent.СreateFavoriteItemEvent(item.id))
        }
        else{
            viewModel.setStateEvent(SearchStateEvent.DeleteFavoriteItemEvent(item.id))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        blog_post_recyclerview.adapter = null
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
        blog_post_recyclerview.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun showGuestDialog(){
        activity?.let {
            val guestCounterDialog = GuestCounterDialog(
                it, viewModel.getFilterAdultsCount(),
                viewModel.getFilterChildrenCount(),
                this)
            guestCounterDialog.show()
        }
    }

    override fun onClearBtnListenerCounter() {
        viewModel.clearCounts()
        onBlogSearchOrFilter()
    }

    override fun onCloseBtnListenerCounter() {
    }

    override fun onSaveBtnListenerCounter(adultsCount: Int, childrenCount: Int) {
        viewModel.setAdultCount(adultsCount)
        viewModel.setChildrenCount(childrenCount)
        onBlogSearchOrFilter()
    }

    private fun showDatePickerDialog(){
        activity?.let {
            val datePickerDialog = DateRangePickerDialog(it, getFilterDatesOrEmpty(), this)
            datePickerDialog.show()
        }
    }

    override fun onClearBtnListener() {
        viewModel.clearDateFilter()
    }

    override fun onCloseBtnListener() {
    }

    override fun onSaveBtnListener(dates: List<Date>) {
        viewModel.setStartDateFilter(dates.first())
        viewModel.setEndDateFilter(dates.last())
        onBlogSearchOrFilter()
    }

    private fun getFilterDatesOrEmpty(): List<Date>{
        if (viewModel.getStartDateFilter() == "" || viewModel.getEndDateFilter() == ""){
            return listOf()
        }
        val startDate = DateUtils.convertStringToDate(viewModel.getStartDateFilter())
        val endDate = DateUtils.convertStringToDate(viewModel.getEndDateFilter())

        val dates = listOf(startDate, endDate)
        Log.d("FilterDialog", "dates: ${dates}")
        return dates

    }

    private fun whenChipFiltered(chip: Chip, t: String){
        chip.apply {
            chipStrokeWidth = 0f
            setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)))
            chipIconTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primaryZero))
            text = t
        }
    }

    private fun whenChipEmpty(chip: Chip, t: String){
        chip.apply {
            chipStrokeWidth = 3f
            setTextColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondaryFirst)))
            chipIconTint = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondaryFirst))
            chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            text = t
        }
    }
}



















