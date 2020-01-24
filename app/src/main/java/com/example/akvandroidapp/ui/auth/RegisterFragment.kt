package com.example.akvandroidapp.ui.auth


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
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
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.example.akvandroidapp.ui.auth.state.RegistrationFields
import com.example.akvandroidapp.ui.main.MainActivity
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.sign_up_detail.*
import java.util.*


class RegisterFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_detail, container, false)
    }

    private var password1:String?=null
    private var password2:String?=null
    private var arg_number:String?=null
    private var arg_user_name:String?=null

    private lateinit var body:EditText
    private lateinit var close:ImageButton

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

        sign_detail_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerPassFragment)
        }

        sign_detail_birth_et.setOnClickListener {
            showDatePicker(sign_detail_birth_et.text.toString())
        }

        term_privacy.makeLinks(
            Pair("политикой конфеденциальности", View.OnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_termOfPolicyFragment)
            }),
            Pair("правилами пользования.", View.OnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_privacyPolicyFragment)
            }))


        subscribeObservers()


    }

    private fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            viewState.registrationFields?.let {
                sendCode()
                it.registration_email?.let{sign_detail_email_et.setText(it)}
                it.registration_username?.let{sign_detail_last_name_et.setText(it)

                }
            }

            viewState.sendCodeFields?.let {
                //it.phone?.let { body.setText(it)}
            }


            viewState.verifyCodeFields?.let {
                it.phone?.let { body.setText(it)}
                //navMainActivity()

            }
        })
    }


    private fun showDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_sign_up_valid)
        body = dialog.findViewById(R.id.dialog_sign_up_valid_code_et) as EditText
        close = dialog.findViewById(R.id.dialog_sign_up_valid_close_btn) as ImageButton

        val valid = dialog.findViewById(R.id.dialog_sign_up_valid_btn) as MaterialButton

        val send = dialog.findViewById(R.id.dialog_sign_up_valid_repeat_code_tv) as TextView

        valid.setOnClickListener {
            if (body.text != null) {
                verifyCode()
                dialog.dismiss()

            }
        }
        send.setOnClickListener {
            verifyCode()
            dialog.dismiss()
        }

        close.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun register(){
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

        showDialog()
    }


    private fun sendCode(){
        viewModel.setStateEvent(
            SendCodeEvent(
                arg_number.toString()
            )
        )
    }

    private fun verifyCode(){
        viewModel.setStateEvent(
            VerifyCodeEvent(
                arg_number.toString(),
                body.text.toString()
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

    private fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
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

    private fun showDatePicker(date: String){
        Locale.setDefault(Locale("ru"))

        val dd = date.split('-')[2]
        val mm = date.split('-')[1]
        val yy = date.split('-')[0]

        val dpd = DatePickerDialog(requireContext(), R.style.MySpinnerDatePickerStyle,
            DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                sign_detail_birth_et.setText(("$i-${i2+1}-$i3"))
            }, yy.toInt(), mm.toInt()+1, dd.toInt())

        dpd.show()
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).text = getString(R.string.cancel_)
    }

    private fun getGender(): Int{
        if (sign_detail_gender_group.checkedRadioButtonId == sign_detail_man_btn.id) return 0
        return 1
    }
}
