package com.example.akvandroidapp.ui.main.messages


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.messages.adapter.ChatListAdapter
import com.example.akvandroidapp.ui.main.messages.adapter.RequestListAdapter
import com.example.akvandroidapp.ui.main.profile.support.MyPagerAdapter
import com.example.akvandroidapp.util.Constants.Companion.MAPKIT_API_KEY
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.android.synthetic.main.fragment_chat_main.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_requests.*
import kotlinx.android.synthetic.main.fragment_support_main.*
import kotlinx.android.synthetic.main.map.*
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

        initRecyclerView()
        subscribeObservers()

    }

    private fun subscribeObservers(){
        sessionManager.favoritePostListItem.observe(this, Observer{ dataState ->
            Log.d(TAG, "chat: ${dataState}")

            recyclerAdapter.apply {
                Log.d(TAG, "chat: ${dataState}")

                preloadGlideImages(
                    requestManager = requestManager,
                    list = dataState
                )
                submitList(
                    blogList = dataState,
                    isQueryExhausted = true
                )
            }
        })
    }


    private fun initRecyclerView(){
        fragment_requests_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@RequestFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = RequestListAdapter(requestManager,  this@RequestFragment)
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

        val fragment: Fragment = MessagesDetailFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.сontainer_messages, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
//        viewModel.loadFirstPage().let {
//            resetUI()
//        }
    }

    private  fun resetUI(){
        fragment_requests_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_request.requestFocus()
    }

}

















