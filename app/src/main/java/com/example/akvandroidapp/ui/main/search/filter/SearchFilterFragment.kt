package com.example.akvandroidapp.ui.main.search.filter


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.appyvet.materialrangebar.RangeBar
import com.appyvet.materialrangebar.RangeBar.OnRangeBarChangeListener
import com.example.akvandroidapp.R
import com.example.akvandroidapp.persistence.BlogQueryUtils
import com.example.akvandroidapp.session.FilterUpdateData
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.viewmodel.*
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.header_filter.*
import loadFirstPage
import javax.inject.Inject


class SearchFilterFragment : BaseActivity() {

    private var price_left:Int = 1
    private var price_right:Int = 30000
    private var room_left:Int = 1
    private var room_right:Int = 15
    private var beds_left:Int = 1
    private var beds_right:Int = 30
    private var acc_name:String? = null
    private var sortMethod:String = Constants.FILTER_TYPE1
    private var accomdationListString:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_filter_layout)

        header_filter_close_ibtn.setOnClickListener {
            finish()
        }

        fragment_filter_city_layout.setOnClickListener {
            navFilterCityFragment()
        }

        subscribeObservers()

        fragment_filter_appart_type_layout.setOnClickListener {
            navFilterTypeFragment()
        }

        fragment_filter_udopstva_layout.setOnClickListener {
            navFilterUdopstvaFragment()
        }

        header_filter_drop_tv.setOnClickListener {
            clearAll()
        }

        getRangebarStatus()
        tuneRadioGroup()

        fragment_filter_show_results_btn.setOnClickListener {
            sessionManager.setFilterUpdateData(FilterUpdateData(
                setCityQuery = fragment_filter_city_tv.text.toString(),
                setBlogFilterTypeHouse = Constants.mapTypes.getValue(fragment_filter_appart_type_tv.text.toString()),
                setBlogFilterPriceLeft = price_left,
                setBlogFilterPriceRight = price_right,
                setBlogFilterRoomsLeft = room_left,
                setBlogFilterRoomsRight = room_right,
                setBlogFilterBedsLeft = beds_left,
                setBlogFilterBedsRight = beds_right,
                setBlogOrdering = sortMethod,
                setBlogVerified = fragment_filter_passed_switch.isChecked.toString(),
                setBlogFilterAccomadations = accomdationListString
            ))
            finish()
        }
    }

    override fun expandAppBar() {
    }

    override fun displayProgressBar(bool: Boolean) {
    }

    private fun subscribeObservers(){
        sessionManager.checkedFilterCity.observe(this, Observer{ dataState ->
            Log.d(TAG, "favorite: ${dataState.location}")
            fragment_filter_city_tv.text = dataState.location
        })

        sessionManager.typeOfApartment.observe(this, Observer{ dataState ->
            Log.d(TAG, "favorite: ${dataState}")
            fragment_filter_appart_type_tv.text = Constants.mapTypesReversed.getValue(dataState)
        })

        sessionManager.facilitiesList.observe(this, Observer{ dataState ->
            accomdationListString = dataState.joinToString(separator = ",")
            Log.d(TAG, "favorite: $accomdationListString")
            if (dataState.size > 0)
                fragment_filter_udopstva_layout_tv.text = getString(R.string.yes)
            else
                fragment_filter_udopstva_layout_tv.text = getString(R.string.no )
        })
    }

    private fun tuneRadioGroup(){
        fragment_filter_sort_radio_group.setOnCheckedChangeListener{_, i ->
            when(i) {
                fragment_filter_no_radio_btn.id -> {
                    sortMethod = Constants.FILTER_TYPE1
                }
                fragment_filter_by_rating_radio_btn.id -> {
                    sortMethod = Constants.FILTER_TYPE2
                }
                fragment_filter_by_price_radio_btn.id -> {
                    sortMethod = Constants.FILTER_TYPE3
                }
            }
        }
    }

    private fun clearAll(){
        fragment_filter_passed_switch.isChecked = false
        fragment_filter_appart_type_tv.text = getString(R.string.any)
        sessionManager.clearTypeOfApartment()
        sessionManager.clearFilterCity()
        sessionManager.clearFilterFacilities()
        fragment_filter_sort_radio_group.check(fragment_filter_no_radio_btn.id)
        fragment_filter_price_rangeBar.setRangePinsByValue(1.toFloat(), 30000.toFloat())
        fragment_filter_room_count_rangeBar.setRangePinsByValue(1.toFloat(), 15.toFloat())
        fragment_filter_bed_count_rangeBar.setRangePinsByValue(1.toFloat(), 30.toFloat())
        fragment_filter_city_tv.text = getString(R.string.no)
        fragment_filter_udopstva_layout_tv.text = getString(R.string.no)
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
        val intent = Intent(this,FilterCityFragment::class.java)
        startActivity(intent)
    }

    private fun navFilterTypeFragment(){
        val intent = Intent(this,FilterTypeFragment::class.java)
        startActivity(intent)
    }

    private fun navFilterUdopstvaFragment(){
        val intent = Intent(this,FilterUdopstvaFragment::class.java)
        startActivity(intent)
    }
}