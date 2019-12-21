package com.example.akvandroidapp.ui.main.search



import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.header_searcher_base_layout.*
import kotlinx.android.synthetic.main.search_part_layout.*
import loadFirstPage
import nextPage
import javax.inject.Inject


class SearchFragment : BaseSearchFragment(), SearchListAdapter.Interaction,SearchListAdapter.InteractionCheck, SwipeRefreshLayout.OnRefreshListener{

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
//            viewModel.loadFirstPage()
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

        by_date_chip.setOnClickListener {
            showDateDialog()
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
                if(viewState.blogFields.blogList.isNotEmpty()){
                    recyclerAdapter.apply {
                        Log.d(TAG, "Search results responses: ${viewState.blogFields.blogList}")
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
                }
            }
        })
    }

    private fun initSearchView(){

//        activity?.apply {
//            val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            searchView = findViewById<ImageButton>(R.id.main_search_et).actionView as SearchView
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            searchView.maxWidth = Integer.MAX_VALUE
//            searchView.setIconifiedByDefault(true)
//            searchView.isSubmitButtonEnabled = true
//        }

//        main_search_et.setOnEditorActionListener { v, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED
//                || actionId == EditorInfo.IME_ACTION_SEARCH ) {
//                val searchQuery = v.text.toString()
//                Log.e(TAG, "SearchView: (keyboard or arrow) executing search...: ${searchQuery}")
//                viewModel.setQuery(searchQuery).let{
//                    onBlogSearchOrFilter()
//                }
//            }
//            true
//        }

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
        viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_searchFragment_to_zhilyeFragment)
    }

    override fun onItemSelected(position: Int, item: BlogPost, boolean: Boolean) {

        Log.d("favorite", "favorite search ${item} and ${boolean}")

        sessionManager.favorite(item,boolean)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
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


//    fun showFilterDialog(){
//
//        Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show()
//        activity?.let {
//            val dialog = MaterialDialog(it)
//                .noAutoDismiss()
//                .customView(R.layout.layout_blog_filter)
//
//            val view = dialog.getCustomView()
//
//            val filter = viewModel.getFilter()
//            val order = viewModel.getOrder()
//
//            view.findViewById<RadioGroup>(R.id.filter_group).apply {
//                when (filter) {
//                    6 -> check(R.id.filter_date)
//                    3 -> check(R.id.filter_author)
//                }
//            }
//
//            view.findViewById<RadioGroup>(R.id.order_group).apply {
//                when (order) {
//                    3 -> check(R.id.filter_asc)
//                    6 -> check(R.id.filter_desc)
//                }
//            }
//
//            view.findViewById<TextView>(R.id.positive_button).setOnClickListener {
//                Log.d(TAG, "FilterDialog: apply filter.")
//
//                val newFilter =
//                    when (view.findViewById<RadioGroup>(R.id.filter_group).checkedRadioButtonId) {
//                        R.id.filter_author -> 6
//                        R.id.filter_date -> 3
//                        else -> 6
//                    }
//
//                val newOrder =
//                    when (view.findViewById<RadioGroup>(R.id.order_group).checkedRadioButtonId) {
//                        R.id.filter_desc -> 3
//                        else -> 6
//                    }
//
//                viewModel.apply {
//                    saveFilterOptions(newFilter, newOrder)
//                    setBlogFilter(newFilter)
//                    setBlogOrder(newOrder)
//                }
//
//                onBlogSearchOrFilter()
//
//                dialog.dismiss()
//            }
//
//            view.findViewById<TextView>(R.id.negative_button).setOnClickListener {
//                Log.d(TAG, "FilterDialog: cancelling filter.")
//                dialog.dismiss()
//            }
//
//            dialog.show()
//        }
//    }


    private fun showDateDialog(){

        activity?.let {
            val dialog = MaterialDialog(it)
                .noAutoDismiss()
                .customView(R.layout.dialog_guests)

            val view = dialog.getCustomView()

            view.findViewById<ImageButton>(R.id.dialog_date_cancel).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}



















