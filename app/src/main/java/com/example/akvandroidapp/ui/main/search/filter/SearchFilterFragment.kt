package com.example.akvandroidapp.ui.main.search.filter


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.header_filter.*


class SearchFilterFragment : BaseSearchFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "SearchFilterFragment: ${viewModel}")

        fragment_filter_city_layout.setOnClickListener {
            navFilterCityFragment()
        }


        fragment_filter_appart_type_layout.setOnClickListener {
            navFilterTypeFragment()
        }

        header_filter_close_ibtn.setOnClickListener {
            findNavController().navigateUp()
        }

    }


    private fun navFilterCityFragment(){
        findNavController().navigate(R.id.action_searchFilterFragment_to_filterCityFragment)
    }

    private fun navFilterTypeFragment(){
        findNavController().navigate(R.id.action_searchFilterFragment_to_filterTypeFragment)
    }



}