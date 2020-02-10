package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.ZhilyeDetail
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import handleIncomingRequest
import kotlinx.android.synthetic.main.fragment_zhilye_book.*
import kotlinx.android.synthetic.main.fragment_zhilye_book_layout.*
import java.lang.Math.round
import java.util.*
import javax.inject.Inject


class ZhilyeBookActivity : BaseActivity() {

    lateinit var stateChangeListener: DataStateChangeListener
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: ZhilyeBookViewModel

    private var validateDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_zhilye_book_layout)

        Locale.setDefault(Locale.forLanguageTag("ru"))

        viewModel = ViewModelProvider(this, providerFactory).get(ZhilyeBookViewModel::class.java)
        stateChangeListener = this

        val bundle = intent.extras?.getBundle("booking")
        val zhilyeDetail = bundle?.getParcelable<ZhilyeDetail>("zhilyeDetail")
        val houseId = bundle?.getInt("house_id", -1)?: -1
        val adults = bundle?.getInt("adultsCounter",0)?: 0
        val children = bundle?.getInt("children",0)?: 0
        val bundleDates = bundle?.getParcelableArrayList("datesList") ?: listOf<DateUtils.DateBundle>()
        val photo = bundle?.getString("zhilyePhoto", null)
        Log.d("ZhilyeBookActivity", "selected dates: $bundleDates")

        val dates = mutableListOf<Date>()
        for (date in bundleDates)
            dates.add(date.date)

        fragment_zhilye_book_title_tv.text = zhilyeDetail?.name
        fragment_zhilye_book_address_tv.text = zhilyeDetail?.address

        if (photo != null)
            Glide.with(this)
                .load(photo)
                .error(R.drawable.test_image_back)
                .into(fragment_zhilye_book_iv)

        fragment_zhilye_book_guests_tv.text = (adults+children).toString()
        fragment_zhilye_book_arrival_tv.text = DateUtils.convertDateToStringForBooking(dates.first())
        fragment_zhilye_book_departure_tv.text = DateUtils.convertDateToStringForBooking(dates.last())
        fragment_zhilye_book_nights_lb.text = ("${zhilyeDetail?.price}kzt x ${dates.size} ночей")
        fragment_zhilye_book_nights_tv.text = ("${(zhilyeDetail?.price?:0) * dates.size}kzt")
        fragment_zhilye_book_tax_tv.text = ("${
            (zhilyeDetail?.price?: 0) * dates.size * Constants.AKV_TAX / 100}kzt"
        )
        fragment_zhilye_book_total_tv.text = ("${
        (zhilyeDetail?.price?: 0) * dates.size * Constants.AKV_TAX / 100 + ((zhilyeDetail?.price?: 0) * dates.size)
        }kzt")

        validateDate = DateUtils.convertDateToString(dates.first())

        setToolbar()
        subscribeObservers()

        fragment_zhilye_book_nex_btn.setOnClickListener {
            sendRequestReservation(dates, (adults+children), houseId)
        }
    }

    private fun subscribeObservers(){

        viewModel.dataState.observe(this, androidx.lifecycle.Observer{ dataState ->
            if(dataState != null) {
                stateChangeListener.onDataStateChange(dataState)
                handleResponse(dataState)
            }
        })

        viewModel.viewState.observe(this, androidx.lifecycle.Observer{ viewState ->
            if(viewState != null){
                val res = viewState.reservationRequestField.response
                if (res.check_in == validateDate) {
                    onBookRequestDone()
                }
            }
        })
    }

    private fun handleResponse(dataState: DataState<ZhilyeBookViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingRequest(it)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{

            }
        }
    }

    override fun expandAppBar() {
    }

    override fun displayProgressBar(bool: Boolean) {
        if(bool){
            progress_bar_zhilye_book.visibility = View.VISIBLE
        }
        else{
            progress_bar_zhilye_book.visibility = View.GONE
        }
    }

    private fun setToolbar(){
        header_zhilye_book_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_cancel_filter_16dp)
        setSupportActionBar(header_zhilye_book_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_zhilye_book_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun sendRequestReservation(dates: List<Date>, guests: Int, houseId: Int){
        viewModel.setStateEvent(ZhilyeBookStateEvent.ReservationEvent(
            check_in = DateUtils.convertDateToString(dates.first()),
            check_out = DateUtils.convertDateToString(dates.last()),
            guests = guests,
            houseId = houseId
        ))
    }

    private fun onBookRequestDone(){
        Toast.makeText(this, "Reservation done", Toast.LENGTH_SHORT).show()
        finish()
    }

}