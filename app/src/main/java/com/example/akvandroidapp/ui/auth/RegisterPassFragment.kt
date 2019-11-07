package com.example.akvandroidapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.sign_up_pass.*


class RegisterPassFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterPassFragment: ${viewModel}")

        nickname_next_btn.setOnClickListener {
            navNextNavigationPage()
        }

        sign_up_pass_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_registerPassFragment_to_registerUpFragment)
        }
    }

    private fun navNextNavigationPage(){
        val arg_number = arguments?.getString("number")
        val arg_user_name = arguments?.getString("arg_user_name")

        val password1 = sign_up_pass_password_et.text.toString()
        val password2 = sign_up_pass_password_re_et.text.toString()

        if(password1.trim() == "" && password2.trim() == "") {
            sign_up_pass_password_l_et.error = getString(R.string.invalid)
            sign_up_pass_password_re_l_et.error = getString(R.string.invalid)
        }

        if(password1.trim() != password2.trim()) sign_up_pass_password_re_et.error = getString(R.string.invalid_pass)

        if(password1.trim() == "") sign_up_pass_password_l_et.error = getString(R.string.invalid)
        else sign_up_pass_password_l_et.isErrorEnabled = false
        if(password2.trim() == "") sign_up_pass_password_re_l_et.error = getString(R.string.invalid)
        else sign_up_pass_password_re_l_et.isErrorEnabled = false

        if(password1.trim() != "" && password2.trim() != "" && password1.trim() == password2.trim()){
            val bundle = bundleOf("password1" to password1, "password2" to password2,
                "arg_number" to arg_number, "arg_user_name" to arg_user_name)
            findNavController().navigate(R.id.action_register_passFragment_to_registerFragment,bundle)
        }
    }
}