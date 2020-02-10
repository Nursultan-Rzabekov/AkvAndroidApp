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
import com.example.akvandroidapp.ui.main.messages.MessagesDetailActivity
import com.example.akvandroidapp.ui.main.search.dialogs.DateRangePickerDialog
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.example.akvandroidapp.util.Constants
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
    SwipeRefreshLayout.OnRefreshListener{

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
        val bundle = bundleOf("houseId" to item.id)
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
            val dialog = Dialog(it, R.style.CustomBasicDialog).apply {
                setCancelable(false)
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setDimAmount(0F)
                setContentView(R.layout.dialog_guests)

                val adults = findViewById<TextView>(R.id.dialog_guests_adult_tv)
                val children = findViewById<TextView>(R.id.dialog_guests_children_tv)

                adultsCount = viewModel.getFilterAdultsCount()
                childrenCount = viewModel.getFilterChildrenCount()

                adults.text = adultsCount.toString()
                children.text = childrenCount.toString()

                findViewById<ImageButton>(R.id.dialog_date_cancel).setOnClickListener {
                    Log.d(TAG, "FilterDialog: cancelling filter.")
                    dismiss()
                }

                findViewById<MaterialButton>(R.id.dialog_guests_save_btn).setOnClickListener {
                    Log.d(TAG, "FilterDialog: save filter.")
                    viewModel.setAdultCount(adultsCount)
                    viewModel.setChildrenCount(childrenCount)
                    onBlogSearchOrFilter()
                    dismiss()
                }

                findViewById<MaterialButton>(R.id.dialog_guests_clear_all_btn).setOnClickListener {
                    viewModel.clearCounts()
                    adultsCount = 0
                    childrenCount = 0
                    adults.text = adultsCount.toString()
                    children.text = childrenCount.toString()
                }

                findViewById<ImageButton>(R.id.dialog_guests_adult_minus_btn).setOnClickListener {
                    if (adultsCount > 1)
                        adultsCount -= 1
                    adults.text = adultsCount.toString()
                }

                findViewById<ImageButton>(R.id.dialog_guests_adult_plus_btn).setOnClickListener {
                    adultsCount += 1
                    adults.text = adultsCount.toString()
                }

                findViewById<ImageButton>(R.id.dialog_guests_children_minus_btn).setOnClickListener {
                    if (childrenCount > 0)
                        childrenCount -= 1
                    children.text = childrenCount.toString()
                }

                findViewById<ImageButton>(R.id.dialog_guests_children_plus_btn).setOnClickListener {
                    if (adultsCount == 0)
                        adultsCount += 1
                    childrenCount += 1
                    children.text = childrenCount.toString()
                    adults.text = adultsCount.toString()
                }

                show()
            }
        }
    }

    private fun showDatePickerDialog(){
        activity?.let {
            val dialog = Dialog(it, R.style.CustomBasicDialog).apply {
                setCancelable(false)
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setDimAmount(0F)
                setContentView(R.layout.dialog_filter_dates)

                val lastyear = Calendar.getInstance()
                lastyear.add(Calendar.YEAR, 0)
                lastyear.add(Calendar.MONTH, 0)
                lastyear.add(Calendar.DAY_OF_MONTH, 0)

                dialog_filter_dates_picker.deactivateDates(ArrayList())

                val nextyear = Calendar.getInstance()
                nextyear.set(Calendar.YEAR, nextyear.get(Calendar.YEAR)+1)
                nextyear.set(Calendar.MONTH, Calendar.DECEMBER)
                nextyear.set(Calendar.DAY_OF_MONTH, 31)

                dialog_filter_dates_picker
                    .init(lastyear.time, nextyear.time)
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDates(getFilterDatesOrEmpty())

                findViewById<ImageButton>(R.id.dialog_filter_dates_picker_cancel).setOnClickListener {
                    Log.d("FilterDialog", "FilterDialog: cancelling filter.")
                    dismiss()
                }

                findViewById<MaterialButton>(R.id.dialog_filter_dates_save_btn).setOnClickListener {
                    Log.d("FilterDialog", "FilterDialog: save filter.")
                    Log.d("FilterDialog", "FilterDialog: ${dialog_filter_dates_picker.selectedDates}")
                    if (dialog_filter_dates_picker.selectedDates.isNotEmpty()){
                        viewModel.setStartDateFilter(dialog_filter_dates_picker.selectedDates.first())
                        viewModel.setEndDateFilter(dialog_filter_dates_picker.selectedDates.last())
                        onBlogSearchOrFilter()
                    }else{
                        viewModel.clearDateFilter()
                    }
                    dismiss()
                }

                findViewById<MaterialButton>(R.id.dialog_filter_dates_clear_all_btn).setOnClickListener {
                    Log.d("FilterDialog", "FilterDialog: clear filter.")
                    dialog_filter_dates_picker.clearSelectedDates()
                    viewModel.clearDateFilter()
                    Log.d("FilterDialog", "FilterDialog: ${dialog_filter_dates_picker.selectedDates}")
                }

                show()
            }
        }
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



















