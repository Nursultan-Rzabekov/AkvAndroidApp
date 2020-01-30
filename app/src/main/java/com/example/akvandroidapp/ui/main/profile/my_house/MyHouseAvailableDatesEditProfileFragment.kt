package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.PasswordChecker
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.fragment_add_ad_available_dates.*
import kotlinx.android.synthetic.main.fragment_add_ad_rules.*
import kotlinx.android.synthetic.main.sign_up_pass.*
import java.util.*
import javax.inject.Inject


class MyHouseAvailableDatesEditProfileFragment : BaseMyHouseFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_available_dates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setToolbar()
        setObservers()

        fragment_add_ad_available_dates_next_layout.visibility = View.GONE
    }

    private fun setObservers(){
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            initCalendarPicker(it.availableDates?: listOf())
        })
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
            .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
            .withSelectedDates(
                DateUtils.getDatesFromToday(dates)
            )
    }

    private fun setToolbar(){
        fragment_add_ad_available_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_available_toolbar.setNavigationOnClickListener{
            saveAvailableDates()
            findNavController().navigateUp()
        }

        fragment_add_ad_available_cancel.visibility = View.INVISIBLE
    }

    private fun saveAvailableDates(){
        sessionManager.setHouseUpdateDates(
            fragment_add_ad_available_dates_datepicker.selectedDates
        )
    }
}