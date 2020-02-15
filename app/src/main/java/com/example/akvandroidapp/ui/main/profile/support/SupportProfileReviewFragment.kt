package com.example.akvandroidapp.ui.main.profile.support


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.PasswordChecker
import kotlinx.android.synthetic.main.header_support.*
import kotlinx.android.synthetic.main.sign_up_pass.*
import kotlinx.android.synthetic.main.support_main_review_layout.*


class SupportProfileReviewFragment : BaseSupportFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.support_main_review_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setToobar()
    }

    private fun setToobar(){
        header_support_message_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_support_message_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}