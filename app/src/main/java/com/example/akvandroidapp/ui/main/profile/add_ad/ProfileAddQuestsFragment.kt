package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.activity_add_ad.*
import kotlinx.android.synthetic.main.back_button_layout.*
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

    private fun setToolbar(){
        fragment_add_ad_guests_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_guests_toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }

        fragment_add_ad_guests_cancel.setOnClickListener {
            activity?.finish()
        }
    }
}

















