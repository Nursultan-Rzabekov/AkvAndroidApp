package com.example.akvandroidapp.ui.main.profile.account


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.util.PasswordChecker
import com.example.akvandroidapp.util.SuccessHandling
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_profile_account.*
import kotlinx.android.synthetic.main.header_profile_account.*
import kotlinx.android.synthetic.main.sign_up_pass.*
import javax.inject.Inject


class AccountUserProfileFragment : BaseProfileFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

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

        attachProfileInformation()

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


    private fun attachProfileInformation(){
        sessionManager.profileInfo.observe(viewLifecycleOwner, Observer{ dataState ->
            fragment_profile_account_birthdate_tv.text = dataState.birthdate.toString()
            fragment_profile_account_gender_tv.text =
                when (dataState.gender){
                    0 -> getString(R.string.woman)
                    1 -> getString(R.string.man)
                    else -> getString(R.string.man)
                }
            fragment_profile_account_phonenumber_tv.text = dataState.phonenumber.toString()
            fragment_profile_account_email_tv.text = dataState.email.toString()
            header_profile_account_tv.text = dataState.nickname.toString()
            Glide.with(this).load(
                if (dataState.image != null) dataState.image else R.drawable.default_image)
                .into(header_profile_account_civ)
        })
    }

}