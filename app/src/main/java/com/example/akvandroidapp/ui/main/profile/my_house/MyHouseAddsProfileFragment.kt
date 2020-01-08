package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.AddAdInfo
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.favorite.FavoriteListAdapter
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds.*
import kotlinx.android.synthetic.main.fragment_saved_pages_filled.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject


class MyHouseAddsProfileFragment : BaseProfileFragment() ,MyHouseListAdapter.Interaction ,MyHouseListAdapter.InteractionCheck,
    SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerAdapter: MyHouseListAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    var myHousePostList: MutableList<AddAdInfo> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")


        subscribeObservers()
        initRecyclerView()

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        fragment_my_adds_add_more_btn.setOnClickListener {
            navNextFragment()
        }

    }

    private fun subscribeObservers(){
        sessionManager.addAdInfo.observe(this, Observer{ dataState ->
            Log.d(TAG, "favorite: ${dataState}")

            myHousePostList.add(dataState)
            myHousePostList.add(dataState)

            recyclerAdapter.apply {
                Log.d(TAG, "favorite: ${dataState}")

                preloadGlideImages(
                    requestManager = requestManager,
                    list = myHousePostList
                )
                submitList(
                    blogList = myHousePostList,
                    isQueryExhausted = true
                )
            }
        })
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileMyHouseAddsProfileFragment_to_profileAddTypeFragment)
    }

    private fun navNextDetailFragment(){
        findNavController().navigate(R.id.action_profileMyHouseAddsProfileFragment_to_profileMyHouseDetailProfileFragment)
    }

    private fun initRecyclerView(){
        fragment_my_adds_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseAddsProfileFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            recyclerAdapter = MyHouseListAdapter(requestManager,  this@MyHouseAddsProfileFragment,this@MyHouseAddsProfileFragment)

            adapter = recyclerAdapter
        }
    }

    override fun onItemSelected(position: Int, item: AddAdInfo) {
        //viewModel.setBlogPost(item)
        navNextDetailFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragment_my_adds_recycler_view.adapter = null
    }

    override fun onRefresh() {
        swipe_refresh.isRefreshing = false
    }

    override fun onItemSelected(position: Int, item: AddAdInfo, boolean: Boolean) {
        recyclerAdapter.removeAt(position)
    }
}