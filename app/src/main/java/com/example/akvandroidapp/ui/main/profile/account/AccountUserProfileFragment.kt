package com.example.akvandroidapp.ui.main.profile.account


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.PasswordChecker
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.header_profile_account.*
import kotlinx.android.synthetic.main.sign_up_pass.*


class AccountUserProfileFragment : BaseProfileFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_account_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        header_profile_account_change_profile_btn.setOnClickListener {
            navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileAccountUserProfileFragment_to_profileAccountUserEditProfileFragment)
    }

}