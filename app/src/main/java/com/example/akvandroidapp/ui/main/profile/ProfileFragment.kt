package com.example.akvandroidapp.ui.main.profile


import android.os.Bundle
import android.view.*
import com.example.akvandroidapp.R


class ProfileFragment : BaseProfileFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.privacy_policy, container, false)
    }
}

















