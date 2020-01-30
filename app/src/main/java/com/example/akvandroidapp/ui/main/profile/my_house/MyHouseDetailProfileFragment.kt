package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.HouseUpdateData
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.DateUtils
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds_change.*
import kotlinx.android.synthetic.main.fragment_my_adds_detailed.*
import kotlinx.android.synthetic.main.fragment_my_adds_detailed_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.my_adds_recycler_view_item.view.*
import java.util.*
import javax.inject.Inject


class MyHouseDetailProfileFragment : BaseMyHouseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_detailed_layout, container, false)
    }

    @Inject
    lateinit var sessionManager: SessionManager

    private var argument:BlogPost? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")

        setToolbar()
        initCalendar()

        argument = arguments?.getParcelable("item")

        fragment_my_adds_detailed_title_tv.text = argument?.name

        Glide.with(this)
            .load(argument?.image)
            .error(R.drawable.test_image_back)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(fragment_my_adds_detailed_iv)

        earning.setOnClickListener {
            navNextFragment()
        }

        fragment_my_adds_detailed_change_btn.setOnClickListener {
            navNextDetailEditFragment()
        }
    }

    private fun navNextFragment(){
        val bundle = Bundle().apply {
            putParcelableArrayList("earnings", ArrayList(getEarnings()))
        }
        findNavController().navigate(R.id.action_profileMyHouseDetailProfileFragment_to_myHouseMoneyProfileFragment, bundle)
    }

    private fun navNextDetailEditFragment(){
        sessionManager.setHouseUpdateData(
            HouseUpdateData(
                -1,
                argument?.name,
                argument?.name,
                price = argument?.price,
                address = argument?.house_type.toString())
        )
        sessionManager.setHouseUpdateFacilityItem(listOf("Утюг"), true)
        findNavController().navigate(R.id.action_profileMyHouseDetailProfileFragment_to_myHouseDetailEditProfileFragment)
    }

    private fun initCalendar(){

        val lastyear = Calendar.getInstance()
        lastyear.add(Calendar.YEAR, 0)
        lastyear.add(Calendar.MONTH, 0)
        lastyear.add(Calendar.DAY_OF_MONTH, 0)
        lastyear.set(Calendar.DAY_OF_MONTH, 1)

        val nextyear = Calendar.getInstance()
        nextyear.set(Calendar.YEAR, nextyear.get(Calendar.YEAR)+1)
        nextyear.set(Calendar.MONTH, Calendar.DECEMBER)
        nextyear.set(Calendar.DAY_OF_MONTH, 31)

        fragment_my_adds_detailed_calendar.isEnabled = false

        fragment_my_adds_detailed_calendar
            .init(lastyear.time, nextyear.time)
            .inMode(CalendarPickerView.SelectionMode.MULTIPLE)
            .withHighlightedDates(
                getBlockedDates()
            )

    }

    private fun getBlockedDates(): List<Date>{
        return listOf(
            DateUtils.convertStringToDate("2020-01-28"),
            DateUtils.convertStringToDate("2020-02-27"),
            DateUtils.convertStringToDate("2020-02-28"),
            DateUtils.convertStringToDate("2020-02-15"),
            DateUtils.convertStringToDate("2020-02-16"),
            DateUtils.convertStringToDate("2020-02-17"),
            DateUtils.convertStringToDate("2020-02-18")
        )
    }

    private fun getEarnings(): List<HouseEarning>{
        return listOf(
            HouseEarning(-1, "asda", 25000, Date())
        )
    }

    private fun setToolbar(){
        fragment_my_adds_detailed_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_my_adds_detailed_toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }
    }

}