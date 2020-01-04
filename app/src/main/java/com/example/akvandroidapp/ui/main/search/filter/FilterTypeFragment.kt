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
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_type.*
import javax.inject.Inject


class FilterTypeFragment : BaseSearchFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_type_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setType()

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }


    }

    private fun setType(){
        Log.e("GroupRai", sessionManager.typeOfApartment.value.toString())
        when(sessionManager.typeOfApartment.value){
            fragment_type_any_tv.text.toString()->{
                fragment_type_any_checked_rbtn.isChecked = true
                fragment_type_apparts_checked_rbtn.isChecked = false
                fragment_type_home_checked_rbtn.isChecked = false

                fragment_type_any_tv.setTextColor(Color.parseColor("#CD3232"))
                fragment_type_apparts_tv.setTextColor(Color.BLACK)
                fragment_type_home_tv.setTextColor(Color.BLACK)
            }

            fragment_type_apparts_tv.text.toString()->{
                fragment_type_any_checked_rbtn.isChecked = false
                fragment_type_apparts_checked_rbtn.isChecked = true
                fragment_type_home_checked_rbtn.isChecked = false

                fragment_type_any_tv.setTextColor(Color.BLACK)
                fragment_type_apparts_tv.setTextColor(Color.parseColor("#CD3232"))
                fragment_type_home_tv.setTextColor(Color.BLACK)
            }

            fragment_type_home_tv.text.toString()->{
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

            sessionManager.filterTypeOfApartment("Дом")
            findNavController().navigateUp()
        }

        fragment_type_apparts_layout.setOnClickListener {
            fragment_type_any_checked_rbtn.isChecked = false
            fragment_type_apparts_checked_rbtn.isChecked = true
            fragment_type_home_checked_rbtn.isChecked = false

            fragment_type_any_tv.setTextColor(Color.BLACK)
            fragment_type_apparts_tv.setTextColor(Color.parseColor("#CD3232"))
            fragment_type_home_tv.setTextColor(Color.BLACK)

            sessionManager.filterTypeOfApartment("Квартира")
            findNavController().navigateUp()
        }

        fragment_type_any_layout.setOnClickListener {
            fragment_type_any_checked_rbtn.isChecked = true
            fragment_type_apparts_checked_rbtn.isChecked = false
            fragment_type_home_checked_rbtn.isChecked = false

            fragment_type_any_tv.setTextColor(Color.parseColor("#CD3232"))
            fragment_type_apparts_tv.setTextColor(Color.BLACK)
            fragment_type_home_tv.setTextColor(Color.BLACK)

            sessionManager.filterTypeOfApartment("Любой")
            findNavController().navigateUp()
        }
    }

}