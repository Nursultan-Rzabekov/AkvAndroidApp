package com.example.akvandroidapp.ui.main.messages


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.messages.adapter.ChatListAdapter
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.profile.support.MyPagerAdapter
import com.example.akvandroidapp.ui.main.profile.support.SupportProfileReviewFragment
import com.example.akvandroidapp.ui.main.search.SearchListAdapter
import com.example.akvandroidapp.ui.main.search.viewmodel.setBlogPost
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.util.Constants.Companion.MAPKIT_API_KEY
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.android.synthetic.main.fragment_chat_main.*
import kotlinx.android.synthetic.main.fragment_chats.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_support_main.*
import kotlinx.android.synthetic.main.map.*
import kotlinx.android.synthetic.main.search_part_layout.*
import javax.inject.Inject


class ChatMesFragment : BaseMessagesFragment(),
    ChatListAdapter.Interaction, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: ChatListAdapter

    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_chat.setOnRefreshListener(this)

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

//        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
//            if(dataState != null) {
//                handlePagination(dataState)
//                stateChangeListener.onDataStateChange(dataState)
//            }
//        })
//
//        viewModel.viewState.observe(viewLifecycleOwner, Observer{ viewState ->
//            if(viewState != null){
//                if(viewState.myChatFields.blogList.isNotEmpty()){
//                    recyclerAdapter.apply {
//                        Log.d(TAG, "Search results responses: ${viewState.myChatFields.blogList}")
//
//                        submitList(
//                            blogList = viewState.myChatFields.blogList,
//                            isQueryExhausted = viewState.myChatFields.isQueryExhausted
//                        )
//                    }
//                }
//            }
//        })
    }

//    private fun handlePagination(dataState: DataState<MessagesViewState>){
//        dataState.data?.let {
//            it.data?.let{
//                it.getContentIfNotHandled()?.let{
//                    viewModel.handleIncomingBlogListData(it)
//                }
//            }
//        }
//
//        dataState.error?.let{ event ->
//            event.peekContent().response.message?.let{
//                if(ErrorHandling.isPaginationDone(it)){
//                    event.getContentIfNotHandled()
//                    viewModel.setQueryExhausted(true)
//                }
//            }
//        }
//    }


    private fun initRecyclerView(){
        fragment_chats_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ChatMesFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = ChatListAdapter(requestManager,  this@ChatMesFragment)
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

        val intent = Intent(context,MessagesDetailActivity::class.java)
        startActivity(intent)

//        findNavController().navigate(R.id.action_ChatMesFragment_to_MessagesDetailFragmentt)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        fragment_chats_recycler_view.adapter = null
    }

    override fun onRefresh() {
        onBlogSearchOrFilter()
        swipe_chat.isRefreshing = false
    }

    private fun onBlogSearchOrFilter(){
//        viewModel.loadFirstPage().let {
//            resetUI()
//        }
    }

    private  fun resetUI(){
        fragment_chats_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_chat.requestFocus()
    }

}

















