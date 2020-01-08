package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds_detailed.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header_my_adds_change.*


class MyHouseDetailEditProfileFragment : BaseProfileFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_change_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "MyHouseDetailEditProfileFragment: ${viewModel}")

        header_my_adds_change_cancel.setOnClickListener {
            findNavController().navigateUp()
        }

    }


}