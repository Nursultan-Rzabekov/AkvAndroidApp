package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewModel
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import handleIncomingRequest
import kotlinx.android.synthetic.main.fragment_zhilye_book.*
import kotlinx.android.synthetic.main.fragment_zhilye_book_layout.*
import javax.inject.Inject


class ZhilyeBookActivity : BaseActivity() {

    lateinit var stateChangeListener: DataStateChangeListener
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: ZhilyeBookViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_zhilye_book_layout)

        viewModel = ViewModelProvider(this, providerFactory).get(ZhilyeBookViewModel::class.java)
        stateChangeListener = this

        setToolbar()
        subscribeObservers()

        fragment_zhilye_book_nex_btn.setOnClickListener {
            sendRequestReservation()
        }
    }

    private fun subscribeObservers(){

        viewModel.dataState.observe(this, androidx.lifecycle.Observer{ dataState ->
            if(dataState != null) {
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(this, androidx.lifecycle.Observer{ viewState ->
            if(viewState != null){
                val res = viewState.reservationRequestField.response
                if (res.response)
                    finish()
                Toast.makeText(applicationContext, "$res", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun expandAppBar() {
    }

    override fun displayProgressBar(bool: Boolean) {
    }

    private fun setToolbar(){
        header_zhilye_book_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_cancel_filter_16dp)
        setSupportActionBar(header_zhilye_book_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_zhilye_book_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun sendRequestReservation(){
        viewModel.setStateEvent(ZhilyeBookStateEvent.ReservationEvent(
            check_in = "2012-12-12",
            check_out = "2012-12-12",
            guests = 1,
            houseId = 28
        ))
    }

}