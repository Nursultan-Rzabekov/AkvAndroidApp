package com.akv.akvandroidapprelease.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapprelease.R
import kotlinx.android.synthetic.main.privacy_policy.*


class PrivacyPolicyFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "PrivacyPolicyFragment: ${viewModel}")


        terms_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_privacyPolicyFragment_to_registerFragment)
        }
    }

}
