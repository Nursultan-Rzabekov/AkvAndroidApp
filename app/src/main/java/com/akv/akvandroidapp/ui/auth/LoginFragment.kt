package com.akv.akvandroidapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.auth.state.AuthStateEvent.LoginAttemptEvent
import com.akv.akvandroidapp.ui.auth.state.LoginFields
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.login.*
import javax.inject.Inject


class LoginFragment : BaseAuthFragment() {

    private var phoneNumber:String? = null

    @Inject
    lateinit var sessionManager: SessionManager

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

        setupSuffixSample()

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
                it.login_email?.let{phonenumber_et.setText(it) }
                it.login_password?.let{password_et.setText(it)}
            }
        })


    }

    fun login(){
        val phoneTotal = "+7".plus(phoneNumber)
        if(phoneTotal.trim().length != 12) phonenumber_l_et.error = getString(R.string.invalid_number)
        if(phonenumber_et.text?.trim() == "") phonenumber_et.error = getString(R.string.invalid)
        else phonenumber_l_et.isErrorEnabled = false
        if(password_et.text?.trim() == "") password_et.error = getString(R.string.invalid)
        else password_l_et.isErrorEnabled = false

        if(phonenumber_et.text?.trim() != "" && password_et.text?.trim() != "") {
            viewModel.setStateEvent(
                LoginAttemptEvent(
                    phoneTotal,
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
        findNavController().navigate(R.id.action_loginFragment_to_register_up_Fragment)
    }

    fun navForgetLoginFragment(){
        findNavController().navigate(R.id.action_loginFragment_to_LoginGmailFragment)
    }

    private fun setupSuffixSample() {
        val affineFormats: MutableList<String> = ArrayList()
        affineFormats.add("+7 ([000]) [000]-[00]-[00]")
        val listener =
            MaskedTextChangedListener.installOn(
                phonenumber_et,
                "+7 ([000]) [000]-[00]-[00]",
                affineFormats, AffinityCalculationStrategy.WHOLE_STRING,
                object : MaskedTextChangedListener.ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, @NonNull extractedValue: String, @NonNull formattedValue: String) {
                        logValueListener(extractedValue)
                    }
                }
            )
        phonenumber_et.hint = listener.placeholder()
    }

    private fun logValueListener(
        extractedValue: String
    ) {
        phoneNumber  = extractedValue
    }
}
















