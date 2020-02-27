package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.setHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.example.akvandroidapp.ui.main.search.zhilye.adapters.ReviewsPageAdapter
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.ErrorHandling
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import com.savvi.rangedatepicker.CalendarPickerView
import handleIncomingBlogListData
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_available_dates.*
import kotlinx.android.synthetic.main.fragment_explore_active.*
import kotlinx.android.synthetic.main.fragment_review_page_layout.*
import kotlinx.android.synthetic.main.fragment_reviews_page.*
import kotlinx.android.synthetic.main.search_part_layout.*
import loadFirstPage
import nextPage
import java.util.*
import javax.inject.Inject


class ZhilyeDatesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_ad_available_dates)
        subscribeObservers()

        setToolbar()

        fragment_add_ad_available_dates_next_layout.visibility = View.GONE
        fragment_add_ad_available_dates_tv2.visibility = View.GONE

        val passedIntent = intent.extras
        val bundle = passedIntent?.getBundle("dates")
        val bundle_dates = bundle?.getParcelableArrayList<BundleDateWrapper>("blocked_dates")

        val dates = mutableListOf<Date>()
        bundle_dates?.forEach{
            dates.add(
                DateUtils.convertStringToDate(it.date)
            )
        }
        val mookup = listOf(
            "2020-2-28"
            )
        mookup.forEach {
            dates.add(DateUtils.convertStringToDate(it))
        }
        initCalendarPicker(dates)
    }

    override fun expandAppBar() {

    }

    private fun subscribeObservers(){

    }

    override fun displayProgressBar(bool: Boolean) {

    }

    private fun initCalendarPicker(dates: List<Date>){
        val lastyear = Calendar.getInstance()
        lastyear.add(Calendar.YEAR, 0)
        lastyear.add(Calendar.MONTH, 0)
        lastyear.add(Calendar.DAY_OF_MONTH, 0)

        val nextyear = Calendar.getInstance()
        nextyear.set(Calendar.YEAR, nextyear.get(Calendar.YEAR)+1)
        nextyear.set(Calendar.MONTH, Calendar.DECEMBER)
        nextyear.set(Calendar.DAY_OF_MONTH, 31)

        fragment_add_ad_available_dates_datepicker
            .init(lastyear.time, nextyear.time)
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withHighlightedDates(DateUtils.getDatesFromToday(dates))
            .displayOnly()
    }

    private fun setToolbar(){
        fragment_add_ad_available_toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        fragment_add_ad_available_cancel.visibility = View.GONE

        fragment_add_ad_available_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    @Parcelize
    data class BundleDateWrapper(
        var date: String
    ): Parcelable

}