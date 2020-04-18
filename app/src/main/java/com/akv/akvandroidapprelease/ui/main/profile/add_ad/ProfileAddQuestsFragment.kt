package com.akv.akvandroidapprelease.ui.main.profile.add_ad


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.session.SessionManager
import kotlinx.android.synthetic.main.fragment_add_ad_guests_count.*
import javax.inject.Inject


class ProfileAddQuestsFragment : BaseAddHouseFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    private var guestsCount: Int = 0
    private var roomsCount: Int = 0
    private var bedsCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_guests_count, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        clearAll()
        setObservers()

        fragment_add_ad_guests_count_next_btn.setOnClickListener {
            navNextFragment()
        }

        fragment_add_ad_guests_count_guest_minus_btn.setOnClickListener {
            if (guestsCount > 0)
                guestsCount--
            fragment_add_ad_guests_count_guest_counter_tv.text = guestsCount.toString()
        }

        fragment_add_ad_guests_count_guest_plus_btn.setOnClickListener {
            fragment_add_ad_guests_count_guest_counter_tv.text = (++guestsCount).toString()
        }

        fragment_add_ad_guests_count_rooms_minus_btn.setOnClickListener {
            if (roomsCount > 0)
                roomsCount--
            fragment_add_ad_guests_count_rooms_counter_tv.text = roomsCount.toString()
        }

        fragment_add_ad_guests_count_rooms_plus_btn.setOnClickListener {
            fragment_add_ad_guests_count_rooms_counter_tv.text = (++roomsCount).toString()
        }

        fragment_add_ad_guests_count_bed_minus_btn.setOnClickListener {
            if (bedsCount > 0)
                bedsCount--
            fragment_add_ad_guests_count_bed_counter_tv.text = bedsCount.toString()
        }

        fragment_add_ad_guests_count_bed_plus_btn.setOnClickListener {
            fragment_add_ad_guests_count_bed_counter_tv.text = (++bedsCount).toString()
        }
    }

    private fun navNextFragment(){
        sessionManager.setAddAdCounts(guestsCount, roomsCount, bedsCount)
        findNavController().navigate(R.id.action_profileAddQuestsFragment_to_profileAddAddressFragment)
    }

    private fun clearAll(){
        fragment_add_ad_guests_count_guest_counter_tv.text = guestsCount.toString()
        fragment_add_ad_guests_count_rooms_counter_tv.text = roomsCount.toString()
        fragment_add_ad_guests_count_bed_counter_tv.text = bedsCount.toString()
    }

    private fun setObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer{
            guestsCount = it._addAdGuestsCount
            roomsCount = it._addAdRoomsCount
            bedsCount = it._addAdBedsCount

            fragment_add_ad_guests_count_guest_counter_tv.text = guestsCount.toString()
            fragment_add_ad_guests_count_rooms_counter_tv.text = roomsCount.toString()
            fragment_add_ad_guests_count_bed_counter_tv.text = bedsCount.toString()
        })
    }

    private fun setToolbar(){
        fragment_add_ad_guests_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_guests_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdCounts()
            findNavController().navigateUp()
        }

        fragment_add_ad_guests_cancel.setOnClickListener {
            sessionManager.clearAddAdAllInfo()
            activity?.finish()
        }
    }
}

















