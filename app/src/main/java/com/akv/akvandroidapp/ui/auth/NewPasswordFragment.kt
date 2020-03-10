package com.akv.akvandroidapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapp.R
import kotlinx.android.synthetic.main.new_pass.*


class NewPasswordFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.new_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "NewPasswordFragment: ${viewModel}")

        login_gmail_send_code_btn.setOnClickListener {
            login()
        }

        new_pass_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_newPasswordFragment_to_resetCodeFragment)
        }

    }

    private fun login(){

        if(new_pass_password_et.text?.trim() == "" && new_pass_password_re_et.text?.trim() == "") {
            new_pass_password_l_et.error = getString(R.string.invalid)
            new_pass_password_re_l_et.error = getString(R.string.invalid)
        }

        if(new_pass_password_et.text?.trim() == "") new_pass_password_l_et.error = getString(R.string.invalid)
        else new_pass_password_l_et.isErrorEnabled = false
        if(new_pass_password_re_et.text?.trim() == "") new_pass_password_re_l_et.error = getString(R.string.invalid)
        else new_pass_password_re_l_et.isErrorEnabled = false

        if(new_pass_password_et.text?.trim() != "" && new_pass_password_re_et.text?.trim() != "" &&
            new_pass_password_et.text?.trim() == new_pass_password_re_et.text?.trim()) {
        }
    }

}
