package com.example.akvandroidapp.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import kotlinx.android.synthetic.main.back_button_layout.*


class ZhilyeRulesOfHouseFragment : BaseSearchFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rules_of_house_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")


        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }


    }



}