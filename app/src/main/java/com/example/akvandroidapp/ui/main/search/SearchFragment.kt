package com.example.akvandroidapp.ui.main.search



import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
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
import com.example.akvandroidapp.ui.main.search.dialogs.DateRangePickerDialog
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import com.google.android.material.button.MaterialButton
import com.savvi.rangedatepicker.CalendarPickerView
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.dialog_filter_dates.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.header_searcher_base_layout.*
import kotlinx.android.synthetic.main.search_part_layout.*
import loadFirstPage
import nextPage
import java.util.*
import javax.inject.Inject


class SearchFragment :
    BaseSearchFragment(),
    SearchListAdapter.Interaction,
    SearchListAdapter.InteractionCheck,
    SwipeRefreshLayout.OnRefreshListener,
    DateRangePickerDialog.DatePickerDialogInteraction{

    private lateinit var recyclerAdapter: SearchListAdapter

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


//        if(savedInstanceState == null){
//            onBlogSearchOrFilter()
//        }

        main_filter_img_btn.setOnClickListener {
            navFilter()
        }

        fragment_explore_apartments_iv.setOnClickListener {
            navApartments()
        }

        fragment_explore_homes_iv.setOnClickListener {
            //navFilter()
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
                        submitList(
                            blogList = viewState.blogFields.blogList,
                            isQueryExhausted = viewState.blogFields.isQueryExhausted
                        )
                    }
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
        sessionManager.favorite(item,boolean)
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
        }
    }

    private  fun resetUI(){
        blog_post_recyclerview.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view.requestFocus()
    }

    private fun showGuestDialog(){

        activity?.let {
            val dialog = Dialog(it, R.style.CustomBasicDialog)
            dialog.setCancelable(false)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setDimAmount(0F)
            dialog.setContentView(R.layout.dialog_guests)

            dialog.findViewById<ImageButton>(R.id.dialog_date_cancel).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.findViewById<MaterialButton>(R.id.dialog_guests_save_btn).setOnClickListener {
                Log.d(TAG, "FilterDialog: save filter.")
            }

            dialog.show()
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

                dialog_filter_dates_picker.init(lastyear.time, nextyear.time)
                    .inMode(CalendarPickerView.SelectionMode.RANGE)

                findViewById<ImageButton>(R.id.dialog_filter_dates_picker_cancel).setOnClickListener {
                    Log.d(TAG, "FilterDialog: cancelling filter.")
                    dismiss()
                }

                findViewById<MaterialButton>(R.id.dialog_filter_dates_save_btn).setOnClickListener {
                    Log.d(TAG, "FilterDialog: save filter.")
                }

                show()
            }
        }
    }

    private fun showCustomDateDialog(){
        val dialog = DateRangePickerDialog(this)
        val ft = fragmentManager?.beginTransaction()
        val prev = fragmentManager?.findFragmentByTag("dialog")
        if (prev != null)
        {
            ft?.remove(prev)
        }
        ft?.addToBackStack(null)
        dialog.show(ft!!, "dialog")
    }

    override fun onCloseBtnListener() {

    }

    override fun onClearBtnListener() {

    }

    override fun onSaveBtnListener() {

    }
}



















