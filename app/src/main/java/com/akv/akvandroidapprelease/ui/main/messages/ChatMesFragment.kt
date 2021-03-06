package com.akv.akvandroidapprelease.ui.main.messages


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.entity.UserChatMessages
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.main.messages.adapter.ChatListAdapter
import com.akv.akvandroidapprelease.ui.main.messages.chatkit.CustomLayoutMessagesActivity
import com.akv.akvandroidapprelease.ui.main.messages.state.MessagesViewState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.setQueryExhausted
import com.akv.akvandroidapprelease.util.AllSidesSpacingItemDecoration
import com.akv.akvandroidapprelease.util.ErrorHandling
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.fragment_chats.*
import loadFirstPage
import nextPage
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

        viewModel.loadFirstPage()

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
                if(viewState.myChatFields.blogList.isNotEmpty()){
                    recyclerAdapter.apply {
                        Log.d(TAG, "Search results responses: ${viewState.myChatFields.blogList}")

                        preloadGlideImages(
                            requestManager = requestManager,
                            list = viewState.myChatFields.blogList
                        )
                        submitList(
                            blogList = viewState.myChatFields.blogList,
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
                    viewModel.handleIncomingBlogListData(it)
                }
            }
        }

        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){
                    event.getContentIfNotHandled()
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }


    private fun initRecyclerView(){
        fragment_chats_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ChatMesFragment.context)
            val topSpacingDecorator = AllSidesSpacingItemDecoration(12)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = ChatListAdapter(requestManager,  this@ChatMesFragment)
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

    override fun onItemSelected(position: Int, item: UserChatMessages) {
        val intent = Intent(context,CustomLayoutMessagesActivity::class.java)
        intent.putExtra("email",item.email)
        intent.putExtra("name",item.first_name)
        intent.putExtra("image",item.userpic)
        intent.putExtra("id",item.id)
        startActivity(intent)
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
        viewModel.loadFirstPage().let {
            resetUI()
        }
    }

    private  fun resetUI(){
        fragment_chats_recycler_view.smoothScrollToPosition(0)
        stateChangeListener.hideSoftKeyboard()
        focusable_view_chat.requestFocus()
    }
}

















