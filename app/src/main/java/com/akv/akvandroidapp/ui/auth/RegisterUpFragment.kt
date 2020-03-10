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
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import com.redmadrobot.inputmask.MaskedTextChangedListener.ValueListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.sign_up.*
import javax.inject.Inject


class RegisterUpFragment : BaseAuthFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    private var phoneNumber:String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterUpFragment: ${viewModel}")

        setupSuffixSample()

        phone_next_btn.setOnClickListener {
            navNextNavigationPage()
        }

        sign_in_tv.setOnClickListener {
            navLogin()
        }

        sessionManager.createAccountUserInfo.observe(viewLifecycleOwner, Observer {
            sign_up_et.hint = it._phoneNumber
        })
    }

    private fun navNextNavigationPage(){
        val phoneTotal = "+7".plus(phoneNumber)
        if(phoneTotal.trim().length != 12) sign_up_l_et.error = getString(R.string.invalid_number)
        else{
//            sign_up_l_et.isErrorEnabled = false
//            val bundle = bundleOf("number" to phoneTotal,"arg_user_name" to argument)
            sessionManager.setCreateAccountPhonenumber(phoneTotal)
            findNavController().navigate(R.id.action_register_upFragment_to_register_pass_Fragment)
        }
    }

    private fun setupSuffixSample() {
        val affineFormats: MutableList<String> = ArrayList()
        affineFormats.add("+7 ([000]) [000]-[00]-[00]")
        val listener =
            installOn(
                sign_up_et,
                "+7 ([000]) [000]-[00]-[00]",
                affineFormats, AffinityCalculationStrategy.WHOLE_STRING,
                object : ValueListener {
                    override fun onTextChanged(maskFilled: Boolean, @NonNull extractedValue: String, @NonNull formattedValue: String) {
                        logValueListener(extractedValue)
                    }
                }
            )
        sign_up_et.hint = listener.placeholder()
    }

    private fun navLogin(){
        findNavController().navigate(R.id.action_registerUpFragment_to_loginFragment)
    }

    private fun logValueListener(
        extractedValue: String
    ) {
        phoneNumber  = extractedValue
    }
}
