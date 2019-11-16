package com.example.akvandroidapp.ui.auth


import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.example.akvandroidapp.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.sign_up_detail.*


class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_detail, container, false)
    }

    val password1 = arguments?.getString("password1")
    val password2 = arguments?.getString("password2")
    val arg_number = arguments?.getString("arg_number")
    val arg_user_name = arguments?.getString("arg_user_name")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterFragment: ${viewModel}")

        sign_detail_create_btn.setOnClickListener {
            register()
        }
        subscribeObservers()

        sign_detail_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerPassFragment)
        }

        term_privacy.makeLinks(
            Pair("политикой конфеденциальности", View.OnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_termOfPolicyFragment)
            }),
            Pair("правилами пользования.", View.OnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_privacyPolicyFragment)
            }))
    }

    fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            viewState.registrationFields?.let {
                it.registration_email?.let{sign_detail_email_et.setText(it)}
                it.registration_username?.let{sign_detail_last_name_et.setText(it)}
            }
        })
    }

    fun register(){
        viewModel.setStateEvent(
            RegisterAttemptEvent(
                sign_detail_email_et.text.toString(),
                arg_user_name.toString(),
                arg_number.toString(),
                password1.toString(),
                sign_detail_last_name_et.text.toString(),
                sign_detail_last_name_et.text.toString(),
                sign_detail_birth_et.text.toString()
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                sign_detail_email_et.text.toString(),
                sign_detail_last_name_et.text.toString(),
                password1.toString(),
                sign_detail_birth_et.text.toString()
            )
        )
    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        this.movementMethod = LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}
