package com.example.akvandroidapp.ui.auth


import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.sign_up.*


class RegisterUpFragment : BaseAuthFragment() {

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


        phone_next_btn.setOnClickListener {
            navNextNavigationPage()
        }

        sign_up_et.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        sign_in_tv.setOnClickListener {
            navLogin()
        }
    }

    private fun navNextNavigationPage(){
        val argument = arguments?.getString("user_name")
        Toast.makeText(context,argument, Toast.LENGTH_SHORT).show()

        val number = sign_up_et.text.toString()

        if(number.trim().equals("")) sign_up_l_et.error = getString(R.string.invalid)
        else{
            sign_up_l_et.isErrorEnabled = false
            val bundle = bundleOf("number" to number,"arg_user_name" to argument)
            findNavController().navigate(R.id.action_register_upFragment_to_register_pass_Fragment,bundle)
        }
    }

    private fun navLogin(){
        findNavController().navigate(R.id.action_registerUpFragment_to_loginFragment)
    }
}
