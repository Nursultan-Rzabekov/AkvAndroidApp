package com.akv.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.entity.ZhilyeBlockedDate
import com.akv.akvandroidapp.ui.BaseActivity
import com.akv.akvandroidapp.ui.displayErrorDialog
import com.akv.akvandroidapp.util.DateUtils
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.fragment_add_ad_available_dates.*
import java.util.*


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
        val bundle_dates = bundle?.getParcelableArrayList<ZhilyeBlockedDate>("blocked_dates")

        val dates = mutableListOf<Date>()
        bundle_dates?.forEach{
            dates.addAll(
                DateUtils.getDatesBetween(
                    DateUtils.convertStringToDate(it.check_in.toString()),
                    DateUtils.convertStringToDate(it.check_out.toString())
                )
            )
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

        try {
            fragment_add_ad_available_dates_datepicker
                .init(lastyear.time, nextyear.time)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withHighlightedDates(DateUtils.getDatesFromToday(dates))
                .displayOnly()
        }catch (e: Exception){
            displayErrorDialog("Something went wrong")
            fragment_add_ad_available_dates_datepicker
                .init(lastyear.time, nextyear.time)
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .displayOnly()
        }
    }

    private fun setToolbar(){
        fragment_add_ad_available_toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        fragment_add_ad_available_cancel.visibility = View.GONE

        fragment_add_ad_available_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

}