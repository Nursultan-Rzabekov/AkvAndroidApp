package com.akv.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.session.SessionManager
import kotlinx.android.synthetic.main.fragment_add_ad_requirement.*
import javax.inject.Inject


class ProfileAddRequirementFragment : BaseAddHouseFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_requirement, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        fragment_add_ad_requirement_next_btn.setOnClickListener {
            navNextFragment()
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddRequirementFragment_to_profileAddRulesFragment)
    }

    private fun setToolbar(){
        fragment_add_ad_requirement_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_requirement_toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }

        fragment_add_ad_requirement_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }
}

















