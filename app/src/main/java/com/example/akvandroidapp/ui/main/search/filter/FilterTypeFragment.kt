package com.example.akvandroidapp.ui.main.search.filter


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_type.*
import javax.inject.Inject


class FilterTypeFragment : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_type_layout)
        setType()

        main_back_img_btn.setOnClickListener {
            finish()
        }
    }

    override fun expandAppBar() {
    }

    override fun displayProgressBar(bool: Boolean) {
    }


    private fun setType(){
        Log.e("GroupRai", sessionManager.typeOfApartment.value.toString())
        when(sessionManager.typeOfApartment.value){
            Constants.mapTypes.getValue(fragment_type_any_tv.text.toString())->{
                fragment_type_any_checked_rbtn.isChecked = true
                fragment_type_apparts_checked_rbtn.isChecked = false
                fragment_type_home_checked_rbtn.isChecked = false

                fragment_type_any_tv.setTextColor(Color.parseColor("#CD3232"))
                fragment_type_apparts_tv.setTextColor(Color.BLACK)
                fragment_type_home_tv.setTextColor(Color.BLACK)
            }

            Constants.mapTypes.getValue(fragment_type_apparts_tv.text.toString())->{
                fragment_type_any_checked_rbtn.isChecked = false
                fragment_type_apparts_checked_rbtn.isChecked = true
                fragment_type_home_checked_rbtn.isChecked = false

                fragment_type_any_tv.setTextColor(Color.BLACK)
                fragment_type_apparts_tv.setTextColor(Color.parseColor("#CD3232"))
                fragment_type_home_tv.setTextColor(Color.BLACK)
            }

            Constants.mapTypes.getValue(fragment_type_home_tv.text.toString())->{
                fragment_type_any_checked_rbtn.isChecked = false
                fragment_type_apparts_checked_rbtn.isChecked = false
                fragment_type_home_checked_rbtn.isChecked = true

                fragment_type_any_tv.setTextColor(Color.BLACK)
                fragment_type_apparts_tv.setTextColor(Color.BLACK)
                fragment_type_home_tv.setTextColor(Color.parseColor("#CD3232"))
            }
        }

        fragment_type_home_layout.setOnClickListener {
            fragment_type_any_checked_rbtn.isChecked = false
            fragment_type_apparts_checked_rbtn.isChecked = false
            fragment_type_home_checked_rbtn.isChecked = true

            fragment_type_any_tv.setTextColor(Color.BLACK)
            fragment_type_apparts_tv.setTextColor(Color.BLACK)
            fragment_type_home_tv.setTextColor(Color.parseColor("#CD3232"))

            sessionManager.filterTypeOfApartment(Constants.mapTypes.getValue("Дом"))
            finish()
        }

        fragment_type_apparts_layout.setOnClickListener {
            fragment_type_any_checked_rbtn.isChecked = false
            fragment_type_apparts_checked_rbtn.isChecked = true
            fragment_type_home_checked_rbtn.isChecked = false

            fragment_type_any_tv.setTextColor(Color.BLACK)
            fragment_type_apparts_tv.setTextColor(Color.parseColor("#CD3232"))
            fragment_type_home_tv.setTextColor(Color.BLACK)

            sessionManager.filterTypeOfApartment(Constants.mapTypes.getValue("Квартира"))
            finish()
        }

        fragment_type_any_layout.setOnClickListener {
            fragment_type_any_checked_rbtn.isChecked = true
            fragment_type_apparts_checked_rbtn.isChecked = false
            fragment_type_home_checked_rbtn.isChecked = false

            fragment_type_any_tv.setTextColor(Color.parseColor("#CD3232"))
            fragment_type_apparts_tv.setTextColor(Color.BLACK)
            fragment_type_home_tv.setTextColor(Color.BLACK)

            sessionManager.filterTypeOfApartment(Constants.mapTypes.getValue("Любой"))
            finish()
        }
    }

}