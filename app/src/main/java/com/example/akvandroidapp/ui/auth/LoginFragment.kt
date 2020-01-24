package com.example.akvandroidapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.example.akvandroidapp.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.login.*


class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "LoginFragment: ${viewModel}")

        sign_in_btn.setOnClickListener {
            login()
        }

        subscribeObservers()

        create_btn.setOnClickListener {
            navLauncherFragment()
        }

        forgot_password_tv.setOnClickListener {
            navForgetLoginFragment()
        }
    }

    fun subscribeObservers(){

        viewModel.viewState.observe(viewLifecycleOwner, Observer{
            it.loginFields?.let{

                it.login_email?.let{phonenumber_et.setText(it)
                    Log.d("Check","Okkkkkkkkkkkkkkkkk")}
                it.login_password?.let{password_et.setText(it)}
            }
        })
    }

    fun login(){

        if(phonenumber_et.text?.trim() == "" && password_et.text?.trim() == "") {
            phonenumber_et.error = getString(R.string.invalid)
            password_et.error = getString(R.string.invalid)
        }

        if(phonenumber_et.text?.trim() == "") password_et.error = getString(R.string.invalid)
        else phonenumber_l_et.isErrorEnabled = false
        if(phonenumber_et.text?.trim() == "") password_et.error = getString(R.string.invalid)
        else password_l_et.isErrorEnabled = false

        if(phonenumber_et.text?.trim() != "" && password_et.text?.trim() != "") {
            viewModel.setStateEvent(
                LoginAttemptEvent(
                    phonenumber_et.text.toString(),
                    password_et.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                phonenumber_et.text.toString(),
                password_et.text.toString()
            )
        )
    }

    fun navLauncherFragment(){
        findNavController().navigate(R.id.action_loginFragment_to_LauncherFragment)
    }

    fun navForgetLoginFragment(){
        findNavController().navigate(R.id.action_loginFragment_to_LoginGmailFragment)
    }
}
















