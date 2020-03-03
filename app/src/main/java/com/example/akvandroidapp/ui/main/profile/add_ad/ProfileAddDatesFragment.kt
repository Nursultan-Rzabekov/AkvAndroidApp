package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.fragment_add_ad_available_dates.*
import java.util.*
import javax.inject.Inject


class ProfileAddDatesFragment : BaseAddHouseFragment() {

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

        setToolbar()
        setObservers()

        fragment_add_ad_available_dates_next_btn.setOnClickListener {
            saveAvailableDates()
            navNextFragment()
        }

    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddDatesFragment_to_profileAddRequirementFragment)
    }

    private fun setObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, androidx.lifecycle.Observer{
            initCalendarPicker(it._addAdAvailableList)
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
            .withSelectedDates(dates)
    }

    private fun setToolbar(){
        fragment_add_ad_available_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_available_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdAvailableDates()
            findNavController().navigateUp()
        }

        fragment_add_ad_available_cancel.setOnClickListener {
            sessionManager.clearAddAdAllInfo()
            activity?.finish()
        }
    }

    private fun saveAvailableDates(){
        sessionManager.setAddAdAvailableDates(
            fragment_add_ad_available_dates_datepicker.selectedDates
        )
    }

    override fun onDestroy() {
        sessionManager.clearAddAdAvailableDates()
        super.onDestroy()
    }
}