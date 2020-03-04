package com.example.akvandroidapp.ui.main.profile.settings


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.fragment_geolocation.*
import kotlinx.android.synthetic.main.fragment_geolocation_layout.*
import javax.inject.Inject


class SettingsGeolocationProfileFragment : BaseProfileFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_geolocation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)

        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsGeolocationProfileFragment: ${viewModel}")

        setGeolocation()
        setToolbar()


    }

    private fun setToolbar(){
        header_geolocation_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_geolocation_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setGeolocation(){
        Log.e("Group Geo", sessionManager.settingsInfo.value?._geolocationState.toString())
        when(sessionManager.settingsInfo.value?._geolocationState){
            Constants.mapGeolocation.getValue(fragment_geolocation_never_layout_tv.text.toString())->{
                fragment_geolocation_never_layout_rbtn.isChecked = true
                fragment_geolocation_on_use_layout_rbtn.isChecked = false
                fragment_geolocation_always_layout_rbtn.isChecked = false

                fragment_geolocation_never_layout_tv.setTextColor(Color.parseColor("#CD3232"))
                fragment_geolocation_on_use_layout_tv.setTextColor(Color.BLACK)
                fragment_geolocation_always_layout_tv.setTextColor(Color.BLACK)
            }

            Constants.mapGeolocation.getValue(fragment_geolocation_on_use_layout_tv.text.toString())->{
                fragment_geolocation_never_layout_rbtn.isChecked = false
                fragment_geolocation_on_use_layout_rbtn.isChecked = true
                fragment_geolocation_always_layout_rbtn.isChecked = false

                fragment_geolocation_never_layout_tv.setTextColor(Color.BLACK)
                fragment_geolocation_on_use_layout_tv.setTextColor(Color.parseColor("#CD3232"))
                fragment_geolocation_always_layout_tv.setTextColor(Color.BLACK)
            }

            Constants.mapGeolocation.getValue(fragment_geolocation_always_layout_tv.text.toString())->{
                fragment_geolocation_never_layout_rbtn.isChecked = false
                fragment_geolocation_on_use_layout_rbtn.isChecked = false
                fragment_geolocation_always_layout_rbtn.isChecked = true

                fragment_geolocation_never_layout_tv.setTextColor(Color.BLACK)
                fragment_geolocation_on_use_layout_tv.setTextColor(Color.BLACK)
                fragment_geolocation_always_layout_tv.setTextColor(Color.parseColor("#CD3232"))
            }
        }

        fragment_geolocation_never_layout.setOnClickListener {
            fragment_geolocation_never_layout_rbtn.isChecked = true
            fragment_geolocation_on_use_layout_rbtn.isChecked = false
            fragment_geolocation_always_layout_rbtn.isChecked = false

            fragment_geolocation_never_layout_tv.setTextColor(Color.parseColor("#CD3232"))
            fragment_geolocation_on_use_layout_tv.setTextColor(Color.BLACK)
            fragment_geolocation_always_layout_tv.setTextColor(Color.BLACK)

            sessionManager.setSettingsGeolocation(Constants.mapGeolocation.getValue(
                fragment_geolocation_never_layout_tv.text.toString()))
            findNavController().navigateUp()
        }

        fragment_geolocation_on_use_layout.setOnClickListener {
            fragment_geolocation_never_layout_rbtn.isChecked = false
            fragment_geolocation_on_use_layout_rbtn.isChecked = true
            fragment_geolocation_always_layout_rbtn.isChecked = false

            fragment_geolocation_never_layout_tv.setTextColor(Color.BLACK)
            fragment_geolocation_on_use_layout_tv.setTextColor(Color.parseColor("#CD3232"))
            fragment_geolocation_always_layout_tv.setTextColor(Color.BLACK)

            sessionManager.setSettingsGeolocation(Constants.mapGeolocation.getValue(
                fragment_geolocation_on_use_layout_tv.text.toString()))
            findNavController().navigateUp()
        }

        fragment_geolocation_always_layout.setOnClickListener {
            fragment_geolocation_never_layout_rbtn.isChecked = false
            fragment_geolocation_on_use_layout_rbtn.isChecked = false
            fragment_geolocation_always_layout_rbtn.isChecked = true

            fragment_geolocation_never_layout_tv.setTextColor(Color.BLACK)
            fragment_geolocation_on_use_layout_tv.setTextColor(Color.BLACK)
            fragment_geolocation_always_layout_tv.setTextColor(Color.parseColor("#CD3232"))

            sessionManager.setSettingsGeolocation(Constants.mapGeolocation.getValue(
                fragment_geolocation_always_layout_tv.text.toString()))
            findNavController().navigateUp()
        }
    }

}