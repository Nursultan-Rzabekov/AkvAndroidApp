package com.akv.akvandroidapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.util.PasswordChecker
import kotlinx.android.synthetic.main.sign_up_pass.*
import javax.inject.Inject


class RegisterPassFragment : BaseAuthFragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_pass, container, false)
    }

//    private var arg_number:String? = null
//    private var arg_user_name:String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterPassFragment: ${viewModel}")

        nickname_next_btn.setOnClickListener {
            navNextNavigationPage()
        }

//        arg_number = arguments?.getString("number")
//        arg_user_name = arguments?.getString("arg_user_name")

        sign_up_pass_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_registerPassFragment_to_registerUpFragment)
        }

        sessionManager.createAccountUserInfo.observe(viewLifecycleOwner, Observer {
            sign_up_pass_password_et.setText(it._password)
            sign_up_pass_password_re_et.setText(it._password)
        })
    }

    private fun navNextNavigationPage(){
        var isFirstPassCorrect = false
        var isSecondPassCorrect = false
        var isNotMatch = false

        val password1 = sign_up_pass_password_et.text.toString()
        val password2 = sign_up_pass_password_re_et.text.toString()

        if(password1.trim() == "") {
            sign_up_pass_password_l_et.isErrorEnabled = true
            sign_up_pass_password_l_et.error = getString(R.string.invalid)}
        else if(!PasswordChecker.checkPassword(password1)) {
            sign_up_pass_password_l_et.isErrorEnabled = false
            sign_up_pass_password_et.error = getString(R.string.invalid_requirments)}
        else {
            isFirstPassCorrect = true
            sign_up_pass_password_l_et.isErrorEnabled = false
        }

        if(password2.trim() == "") {
            sign_up_pass_password_re_l_et.isErrorEnabled = true
            sign_up_pass_password_re_l_et.error = getString(R.string.invalid)}
        else {
            isSecondPassCorrect = true
            sign_up_pass_password_re_l_et.isErrorEnabled = false
        }

        if(password1.trim() != password2.trim() && isFirstPassCorrect && isSecondPassCorrect) {
            isNotMatch = true
            sign_up_pass_password_re_l_et.isErrorEnabled = true
            sign_up_pass_password_re_l_et.error = getString(R.string.invalid_pass)
        }

        if(isFirstPassCorrect && isSecondPassCorrect && !isNotMatch){
//            val bundle = bundleOf("password1" to password1, "password2" to password2,
//                "arg_number" to arg_number, "arg_user_name" to arg_user_name)
            sessionManager.setCreateAccountPassword(password1)
            findNavController().navigate(R.id.action_register_passFragment_to_registerFragment)
        }
    }

}