package com.example.akvandroidapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.login_gmail.*


class LoginGmailFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_gmail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "LoginGmailFragment: ${viewModel}")

        login_gmail_send_code_btn.setOnClickListener{
            navNextNavigationPage()
        }

        login_gmail_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_loginGmailFragment_to_LoginFragment)
        }
    }

    private fun navNextNavigationPage(){
        val gmail = login_gmail_email_et.text.toString()

        if(gmail.trim().equals("")) login_gmail_email_l_et.error = getString(R.string.invalid)
        else{
            login_gmail_email_l_et.isErrorEnabled = false
            val bundle = bundleOf("gmail" to gmail)
            findNavController().navigate(R.id.action_loginGmailFragment_to_ResetCodeFragment,bundle)
        }
    }

}
