package com.example.akvandroidapp.ui.main.favorite


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.SessionManager

import com.example.akvandroidapp.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_saved_pages.*
import kotlinx.android.synthetic.main.fragment_saved_pages_empty.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.*
import javax.inject.Inject


class FavoriteFragment : BaseFavoriteFragment(), FavoriteListAdapter.Interaction ,FavoriteListAdapter.InteractionCheck,
    SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: FavoriteListAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    var favoritePostList: MutableList<BlogPost> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_pages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()

        fragment_saved_pages_empty_find_btn.setOnClickListener {
            fragment_saved_pages_empty_id.visibility = View.GONE
            fragment_saved_pages_filled_id.visibility = View.VISIBLE

            subscribeObservers()
        }
    }

    private fun subscribeObservers(){
        sessionManager.favoritePostListItem.observe(this, Observer{ dataState ->
            Log.d(TAG, "favorite: ${dataState}")

            recyclerAdapter.apply {
                Log.d(TAG, "favorite: ${dataState}")

                favoritePostList = dataState

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
        fragment_saved_pages_filled_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FavoriteFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = FavoriteListAdapter(requestManager,  this@FavoriteFragment,this@FavoriteFragment)

            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: BlogPost) {
        //viewModel.setBlogPost(item)
        findNavController().navigate(R.id.action_favoriteFragment_to_zhilyeFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        fragment_saved_pages_filled_recycler_view.adapter = null
    }

    override fun onRefresh() {
        swipe_refresh.isRefreshing = false
    }

    override fun onItemSelected(position: Int, item: BlogPost, boolean: Boolean) {
        Log.d("qwe","size list ${favoritePostList.size}")
        sessionManager.favorite(item,boolean)
        recyclerAdapter.removeAt(position)
    }
}














