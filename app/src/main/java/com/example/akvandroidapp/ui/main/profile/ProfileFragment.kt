package com.example.akvandroidapp.ui.main.profile


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.fragment_profile_owner.*
import kotlinx.android.synthetic.main.header_profile.*
import kotlinx.android.synthetic.main.profile_part_layout.*


class ProfileFragment : BaseProfileFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_part_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_profile_owner_host_mode_switch.setOnCheckedChangeListener { _, b ->
            if(b){
                fragment_profile_owner_host_mode_ads.visibility = View.VISIBLE
                fragment_profile_owner_post_ad_btn.visibility = View.VISIBLE
            }
            else{
                fragment_profile_owner_host_mode_ads.visibility = View.GONE
                fragment_profile_owner_post_ad_btn.visibility = View.GONE
            }
        }

        fragment_profile_owner_post_ad_btn.setOnClickListener {
            navNextFragment()
        }

        fragment_profile_owner_about_us_layout.setOnClickListener {
            navigateAboutFragment()
        }

        fragment_profile_owner_support_layout.setOnClickListener {
            navigateSupportFragment()
        }

        fragment_profile_owner_settings_layout.setOnClickListener {
            navigateSettingsFragment()
        }

        fragment_profile_owner_my_ads_layout.setOnClickListener {
            navigateMyHouseAddsFragment()
        }

        header_profile_civ.setOnClickListener {
            navigateAccountUserProfileFragment()
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileAddTypeFragment)
    }

    private fun navigateAboutFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileAboutProfileFragment)
    }

    private fun navigateSupportFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_supportActivity)
    }

    private fun navigateSettingsFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileSettingsProfileFragment)
    }

    private fun navigateMyHouseAddsFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileMyHouseAddsProfileFragment)
    }

    private fun navigateAccountUserProfileFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileAccountUserProfileFragment)
    }


}
















