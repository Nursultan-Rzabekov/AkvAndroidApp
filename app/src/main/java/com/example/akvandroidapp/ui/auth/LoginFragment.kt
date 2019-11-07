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
import kotlinx.android.synthetic.main.fragment_login.input_email
import kotlinx.android.synthetic.main.fragment_login.input_password
import kotlinx.android.synthetic.main.fragment_login.login_button
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

        login_button.setOnClickListener {
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
                it.login_email?.let{input_email.setText(it)}
                it.login_password?.let{input_password.setText(it)}
            }
        })
    }

    fun login(){

        if(input_email.text.trim() == "" && input_password.text.trim() == "") {
            input_email.error = getString(R.string.invalid)
            input_password.error = getString(R.string.invalid)
        }

        if(input_email.text.trim() == "") input_email.error = getString(R.string.invalid)
        else phonenumber_l_et.isErrorEnabled = false
        if(input_password.text.trim() == "") input_password.error = getString(R.string.invalid)
        else password_l_et.isErrorEnabled = false

        if(input_email.text.trim() != "" && input_password.text.trim() != "") {
            viewModel.setStateEvent(
                LoginAttemptEvent(
                    input_email.text.toString(),
                    input_password.text.toString()
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setLoginFields(
            LoginFields(
                input_email.text.toString(),
                input_password.text.toString()
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
















