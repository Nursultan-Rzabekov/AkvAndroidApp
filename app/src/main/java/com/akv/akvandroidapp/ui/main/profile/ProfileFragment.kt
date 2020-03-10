package com.akv.akvandroidapp.ui.main.profile


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.main.profile.add_ad.AddAdMainActivity
import com.akv.akvandroidapp.ui.main.profile.my_house.MyHouseMainActivity
import com.akv.akvandroidapp.ui.main.profile.payment.PaymentMainActivity
import com.akv.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.akv.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.akv.akvandroidapp.ui.main.profile.support.SupportMainActivity
import com.akv.akvandroidapp.util.SuccessHandling
import handleIncomingProfileInfo
import kotlinx.android.synthetic.main.fragment_profile_owner.*
import kotlinx.android.synthetic.main.header_profile.*
import javax.inject.Inject


class ProfileFragment : BaseProfileFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_part_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        getInfo()
        subscribeObservers()

        fragment_profile_owner_host_mode_switch.setOnCheckedChangeListener { _, b ->
            onOwnerMode(b)
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

        fragment_profile_owner_payment_layout.setOnClickListener {
            navigatePaymentFragment()
        }

        header_profile_civ.setOnClickListener {
            navigateAccountUserProfileFragment()
        }
    }

    private fun setObservers() {
        sessionManager.isHostMode.observe(viewLifecycleOwner, Observer{ dataState ->
            fragment_profile_owner_host_mode_switch.isChecked = dataState
        })
    }

    private fun navNextFragment(){
        val intent = Intent(context, AddAdMainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateAboutFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileAboutProfileFragment)
    }

    private fun navigateSupportFragment(){
        val intent = Intent(context, SupportMainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateSettingsFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileSettingsProfileFragment)
    }

    private fun navigateMyHouseAddsFragment(){
        val intent = Intent(context, MyHouseMainActivity::class.java)
        startActivity(intent)
    }

    private fun navigatePaymentFragment(){
        val intent = Intent(context, PaymentMainActivity::class.java)
        startActivity(intent)
    }

    private fun navigateAccountUserProfileFragment(){
        findNavController().navigate(R.id.action_profileFragment_to_profileAccountUserProfileFragment)
    }

    private fun onOwnerMode(b: Boolean){
        if(b){
            fragment_profile_owner_host_mode_ads.visibility = View.VISIBLE
            fragment_profile_owner_post_ad_btn.visibility = View.VISIBLE
        }
        else{
            fragment_profile_owner_host_mode_ads.visibility = View.GONE
            fragment_profile_owner_post_ad_btn.visibility = View.GONE
        }
        sessionManager.changeHostMode(b)
    }


    private fun getInfo(){
        viewModel.setStateEvent(ProfileStateEvent.GetProfileInfoEvent)
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            handleUpdate(dataState)
            stateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.response?.let { event ->
                    event.peekContent().let { response ->
                        response.message?.let { message ->
                            if (message == SuccessHandling.SUCCESS_BLOG_CREATED) {
                                viewModel.clearNewBlogFields()
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.profileInfoFields.let{ newBlogFields ->
                header_profile_tv.text = newBlogFields.first_name.toString()
                Glide.with(this).load(
                    if (newBlogFields.newImageUri != null) newBlogFields.newImageUri else R.drawable.user)
                    .into(header_profile_civ)
            }
        })

    }

    private fun handleUpdate(dataState: DataState<ProfileViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingProfileInfo(it)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{

            }
        }
    }
}
















