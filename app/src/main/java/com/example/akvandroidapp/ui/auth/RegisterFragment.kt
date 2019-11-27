package com.example.akvandroidapp.ui.auth


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.example.akvandroidapp.ui.auth.state.RegistrationFields
import kotlinx.android.synthetic.main.sign_up_detail.*
import java.util.*


class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_detail, container, false)
    }

    private var password1:String?=null
    private var password2:String?=null
    private var arg_number:String?=null
    private var arg_user_name:String?=null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterFragment: ${viewModel}")


        password1 = arguments?.getString("password1")
        password2 = arguments?.getString("password2")
        arg_number = arguments?.getString("arg_number")
        arg_user_name = arguments?.getString("arg_user_name")

        sign_detail_create_btn.setOnClickListener {
            register()
        }
        subscribeObservers()


        sign_detail_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerPassFragment)
        }

        sign_detail_birth_et.setOnClickListener {
            showDatePicker()
        }

        sign_detail_gender_group.setOnCheckedChangeListener { radioGroup, i ->
            onGenderChanged(i)
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
                getGender(),
                arg_number.toString(),
                password1.toString(),
                arg_user_name.toString(),
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

    fun showDatePicker(){
        val c = Calendar.getInstance()
        val brYear = c.get(Calendar.YEAR)
        val brMonth = c.get(Calendar.MONTH)
        val brDay = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), R.style.MySpinnerDatePickerStyle,
            DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                sign_detail_birth_et.setText(("$i-$i2-$i3"))
            }, brYear, brMonth, brDay)
        dpd.show()
    }

    fun onGenderChanged(id: Int){
        if (id == sign_detail_man_btn.id){
            sign_detail_man_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            sign_detail_woman_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }else{
            sign_detail_man_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            sign_detail_woman_btn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    fun getGender(): Int{
        if (sign_detail_gender_group.checkedRadioButtonId == sign_detail_man_btn.id) return 1
        return 2
    }
}
