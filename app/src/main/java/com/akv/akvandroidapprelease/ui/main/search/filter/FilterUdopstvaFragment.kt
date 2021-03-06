package com.akv.akvandroidapprelease.ui.main.search.filter


import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.ui.BaseActivity
import com.akv.akvandroidapprelease.util.Constants
import kotlinx.android.synthetic.main.fragment_udopstva.*
import kotlinx.android.synthetic.main.fragment_udopstva_layout.*


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