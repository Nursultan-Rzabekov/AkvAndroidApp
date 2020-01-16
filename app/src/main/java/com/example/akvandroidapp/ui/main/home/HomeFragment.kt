package com.example.akvandroidapp.ui.main.home


import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.messages.adapter.ChatListAdapter
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_saved_booking.*
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

    }

    private fun subscribeObservers(){
    }


    private fun initRecyclerView(){
        fragment_saved_booking_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = HomeListAdapter(requestManager,  this@HomeFragment)
//            addOnScrollListener(object: RecyclerView.OnScrollListener(){
//
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                    val lastPosition = layoutManager.findLastVisibleItemPosition()
//                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
//                        Log.d(TAG, "BlogFragment: attempting to load next page...")
//                        viewModel.nextPage()
//                    }
//                }
//            })
            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        //viewModel.setBlogPost(item)
        //findNavController().navigate(R.id.action_searchFragment_to_zhilyeFragment)
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
//        viewModel.loadFirstPage().let {
//            resetUI()
//        }
    }

    private fun listEmptyState(){
        fragment_saved_booking_lb2.visibility = View.VISIBLE
        swipe_home.visibility = View.GONE
    }

    private fun listFilledState(){
        fragment_saved_booking_lb2.visibility = View.GONE
        swipe_home.visibility = View.VISIBLE
    }

    private  fun resetUI(){
        fragment_saved_booking_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_home.requestFocus()
    }

}