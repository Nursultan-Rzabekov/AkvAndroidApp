package com.example.akvandroidapp.ui.main.profile.settings


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings_layout.*
import javax.inject.Inject


class SettingsProfileFragment : BaseProfileFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")

        initialState()
        setToolbar()

        fragment_settings_region_layout.setOnClickListener {
            navigateRegion()
        }

        fragment_settings_geolocation_layout.setOnClickListener {
            navigateGeolocation()
        }

        fragment_settings_push_switch.setOnCheckedChangeListener { compoundButton, b ->
            sessionManager.setSettingsPushNotifications(b)
        }

        fragment_settings_email_switch.setOnCheckedChangeListener { compoundButton, b ->
            sessionManager.setSettingsEmailNotifications(b)
        }

        fragment_settings_quit_tv.setOnClickListener {
            sessionManager.logout()
        }
    }

    private fun navigateGeolocation(){
        findNavController().navigate(R.id.action_profileSettingsProfileFragmentt_to_profileSettingsGeolocationProfileFragment)
    }

    private fun navigateRegion(){
        findNavController().navigate(R.id.action_profileSettingsProfileFragmentt_to_profileSettingsRegionProfileFragment)
    }

    private fun initialState(){
        sessionManager.settingsInfo.observe(viewLifecycleOwner, Observer { dataState ->
            fragment_settings_push_switch.isChecked = dataState._pushNotificationsOn
            fragment_settings_email_switch.isChecked = dataState._emailNotificationsOn
            fragment_settings_region_tv.text = dataState._region.location
            fragment_settings_geolocation_tv.text = Constants.mapGeolocationReversed.getValue(dataState._geolocationState)
        })
    }

    private fun setToolbar(){
        header_settings_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_settings_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}