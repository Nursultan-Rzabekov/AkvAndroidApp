package com.example.akvandroidapp.ui.main.search.filter


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.appyvet.materialrangebar.RangeBar
import com.appyvet.materialrangebar.RangeBar.OnRangeBarChangeListener
import com.example.akvandroidapp.R
import com.example.akvandroidapp.persistence.BlogQueryUtils
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.header_filter.*
import loadFirstPage


class SearchFilterFragment : BaseSearchFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_layout, container, false)
    }


    private var price_left:Int = 0
    private var price_right:Int = BlogQueryUtils.BLOG_ORDER_PRICE_RIGHT

    private var room_left:Int = 0
    private var room_right:Int = BlogQueryUtils.BLOG_ORDER_PRICE_RIGHT

    private var beds_left:Int = 0
    private var beds_right:Int = BlogQueryUtils.BLOG_ORDER_PRICE_RIGHT

    private var city_name:String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "SearchFilterFragment: ${viewModel}")

        fragment_filter_city_layout.setOnClickListener {
            navFilterCityFragment()
        }

        city_name = arguments?.getString("city")
        fragment_filter_city_tv.text = city_name

        fragment_filter_appart_type_layout.setOnClickListener {
            navFilterTypeFragment()
        }

        fragment_filter_udopstva_layout.setOnClickListener {
            navFilterUdopstvaFragment()
        }

        header_filter_close_ibtn.setOnClickListener {
            findNavController().navigateUp()
        }

        getRangebarStatus()

        val city:String
        if(city_name==null){
            city = "Шымкент"
        }
        else{
            city = city_name.toString()
        }

        fragment_filter_show_results_btn.setOnClickListener {
            viewModel.apply {
                setCityQuery(city)
                setBlogFilterPrice(price_left,price_right)
                setBlogFilterRooms(room_left,room_right)
                setBlogFilterBeds(beds_left,beds_right)
            }
            onBlogSearchOrFilter()
        }
    }

    private fun onBlogSearchOrFilter(){
        viewModel.loadFirstPage().let {
            findNavController().popBackStack()
        }
    }

    private fun getRangebarStatus(){
        fragment_filter_price_rangeBar.setPinTextFormatter(object: RangeBar.PinTextFormatter{
            override fun getText(value: String?): String {
                return value.toString()
            }
        })

        fragment_filter_price_rangeBar.setOnRangeBarChangeListener(object : OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar, leftPinIndex: Int,
                rightPinIndex: Int, leftPinValue: String, rightPinValue: String
            ) {
                Log.d("RangeBar", "Touch Left Value + ${leftPinValue}")
                Log.d("RangeBar", "Touch Right Value + ${rightPinValue}")
                price_left = leftPinValue.toInt()
                price_right = rightPinValue.toInt()
            }

            override fun onTouchEnded(rangeBar: RangeBar) {
                Log.d("RangeBar", "Touch ended")

            }

            override fun onTouchStarted(rangeBar: RangeBar) {
                Log.d("RangeBar", "Touch started")
            }
        })

        fragment_filter_room_count_rangeBar.setOnRangeBarChangeListener(object : OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar, leftPinIndex: Int,
                rightPinIndex: Int, leftPinValue: String, rightPinValue: String
            ) {
                room_left = leftPinValue.toInt()
                room_right = rightPinValue.toInt()
                Log.d("RangeBar", "Touch Left Value + ${room_left}")
                Log.d("RangeBar", "Touch Right Value + ${room_right}")
            }

            override fun onTouchEnded(rangeBar: RangeBar) {
                Log.d("RangeBar", "Touch ended")

            }

            override fun onTouchStarted(rangeBar: RangeBar) {
                Log.d("RangeBar", "Touch started")
            }
        })

        fragment_filter_bed_count_rangeBar.setOnRangeBarChangeListener(object : OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar, leftPinIndex: Int,
                rightPinIndex: Int, leftPinValue: String, rightPinValue: String
            ) {
                beds_left = leftPinValue.toInt()
                beds_right = rightPinValue.toInt()
                Log.d("RangeBar", "Touch Left Value + ${beds_left}")
                Log.d("RangeBar", "Touch Right Value + ${beds_right}")
            }

            override fun onTouchEnded(rangeBar: RangeBar) {
                Log.d("RangeBar", "Touch ended")
            }
            override fun onTouchStarted(rangeBar: RangeBar) {
                Log.d("RangeBar", "Touch started")
            }
        })
    }

    private fun navFilterCityFragment(){
        findNavController().navigate(R.id.action_searchFilterFragment_to_filterCityFragment)
    }

    private fun navFilterTypeFragment(){
        findNavController().navigate(R.id.action_searchFilterFragment_to_filterTypeFragment)
    }

    private fun navFilterUdopstvaFragment(){
        findNavController().navigate(R.id.action_searchFilterFragment_to_filterUdopstvaFragment)
    }

}