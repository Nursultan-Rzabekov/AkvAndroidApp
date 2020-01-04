package com.example.akvandroidapp.ui.main.search.filter


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_udopstva.*
import javax.inject.Inject


class FilterUdopstvaFragment : BaseSearchFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    private val mapFacility: Map<String, List<Any>> = mapOf(
        "Отопление" to listOf(fragment_udopstva_heat_layout, fragment_udopstva_heat_layout_tv, fragment_udopstva_heat_layout_ckb),
        "Wifi" to listOf(fragment_udopstva_wifi_layout, fragment_udopstva_wifi_layout_tv, fragment_udopstva_wifi_layout_ckb),
        "Кондиционер" to listOf(fragment_udopstva_con_layout, fragment_udopstva_con_layout_tv, fragment_udopstva_con_layout_ckb)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_udopstva_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setFacilities()

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setFacilities(){
        for ((facility, views) in mapFacility) {
            if (facility in sessionManager.facilitiesList.value!!) {
                (views[2] as CheckBox).isChecked = true
                (views[1] as TextView).setTextColor(Color.parseColor("#CD3232"))
            }
            Log.e("ConstraintLay", "${views[1]}")
        }

        for ((facility, views) in mapFacility){
            Log.e("ConstraintLay", "${views[0]}")
            (views[0] as ConstraintLayout).setOnClickListener {
                if (facility in sessionManager.facilitiesList.value!!) {
                    sessionManager.setFacilityValue(facility, false)
                    (views[2] as CheckBox).isChecked = false
                    (views[1] as TextView).setTextColor(Color.BLACK)
                }else {
                    sessionManager.setFacilityValue(facility, true)
                    (views[2] as CheckBox).isChecked = true
                    (views[1] as TextView).setTextColor(Color.parseColor("#CD3232"))
                }
            }
        }
    }

}