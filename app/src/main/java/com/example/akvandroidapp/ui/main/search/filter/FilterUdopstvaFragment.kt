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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_udopstva.*
import kotlinx.android.synthetic.main.fragment_udopstva_layout.*
import javax.inject.Inject


class FilterUdopstvaFragment : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_udopstva_layout)

        setAllFacilities()
        setToolbar()

    }

    override fun expandAppBar() {
    }
    override fun displayProgressBar(bool: Boolean) {
    }

    private fun setAllFacilities(){
        setFacilities("Отопление", fragment_udopstva_heat_layout_tv, fragment_udopstva_heat_layout, fragment_udopstva_heat_layout_ckb)
        setFacilities("Wifi", fragment_udopstva_wifi_layout_tv, fragment_udopstva_wifi_layout, fragment_udopstva_wifi_layout_ckb)
        setFacilities("Кондиционер", fragment_udopstva_con_layout_tv, fragment_udopstva_con_layout, fragment_udopstva_con_layout_ckb)
        setFacilities("Аптечка", fragment_udopstva_med_layout_tv, fragment_udopstva_med_layout, fragment_udopstva_med_layout_ckb)
        setFacilities("Чайник", fragment_udopstva_tea_layout_tv, fragment_udopstva_tea_layout, fragment_udopstva_tea_layout_ckb)
        setFacilities("Фен", fragment_udopstva_fen_layout_tv, fragment_udopstva_fen_layout, fragment_udopstva_fen_layout_ckb)
    }

    private fun setFacilities(facility:String, textView: TextView, constraintLayout: ConstraintLayout, checkBox: CheckBox){

        if (Constants.mapFacilities.getValue(facility) in sessionManager.facilitiesList.value!!) {
            checkBox.isChecked = true
            textView.setTextColor(Color.parseColor("#CD3232"))
        }

        constraintLayout.setOnClickListener {
            if (Constants.mapFacilities.getValue(facility) in sessionManager.facilitiesList.value!!) {
                sessionManager.setFacilityValue(Constants.mapFacilities.getValue(facility), false)
                checkBox.isChecked = false
                textView.setTextColor(Color.BLACK)
            }else {
                sessionManager.setFacilityValue(Constants.mapFacilities.getValue(facility), true)
                checkBox.isChecked = true
                textView.setTextColor(Color.parseColor("#CD3232"))
            }
        }
    }

    private fun setToolbar(){
        header_udobstva_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back)
        setSupportActionBar(header_udobstva_toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_udobstva_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

}