package com.akv.akvandroidapp.ui.auth


import android.app.DatePickerDialog
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.auth.dialogs.CodeValidationDialog
import com.akv.akvandroidapp.ui.auth.state.AuthStateEvent.*
import com.akv.akvandroidapp.ui.auth.state.AuthViewState
import com.akv.akvandroidapp.ui.auth.state.RegistrationFields
import com.akv.akvandroidapp.ui.main.search.viewmodel.setQueryExhausted
import com.akv.akvandroidapp.util.Constants
import com.akv.akvandroidapp.util.DateUtils
import com.akv.akvandroidapp.util.ErrorHandling
import handleIncomingBlogListData
import kotlinx.android.synthetic.main.sign_up_detail.*
import java.util.*
import javax.inject.Inject


class RegisterFragment : BaseAuthFragment(), CodeValidationDialog.CodeValidationDialogInteraction {

    @Inject
    lateinit var sessionManager: SessionManager

    private var phonenumber: String? = null
    private var password: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "RegisterFragment: ${viewModel}")

        subscribeObservers()

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

    }

    private fun subscribeObservers(){
        sessionManager.createAccountUserInfo.observe(viewLifecycleOwner, Observer {
            password = it._password
            phonenumber = it._phoneNumber
            Log.d(TAG, "session create cache: password: $password, phonenumber: $phonenumber")
        })

        viewModel.dataState.observe(viewLifecycleOwner, Observer{ dataState ->
            if(dataState != null) {
                Log.d(TAG, "favorites dataState changed")
                handlePagination(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            if (viewState.responseVerify) {
                navigateToLoginFragment()
            }
            viewState.registrationFields?.let {
                    it.registration_email?.let{sign_detail_email_et.setText(it)}
                    it.registration_username?.let{sign_detail_last_name_et.setText(it)
                }
            }
        })

        sessionManager.accountProperties.observe(viewLifecycleOwner, Observer {
            it.let {
                sendCode()
                showDialog()
            }
        })
    }

    private fun navigateToLoginFragment(){
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun handlePagination(dataState: DataState<AuthViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleIncomingBlogListData(it)
                }
            }
        }

        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){
                    event.getContentIfNotHandled()
                    viewModel.setQueryExhausted(true)
                }
            }
        }
    }

    private fun showDialog() {
        activity?.let {
            val codevalidationDialog = CodeValidationDialog(it, this)
            codevalidationDialog.show()
        }
    }

    override fun onCloseBtnListener() {
    }

    override fun onSendMoreBtnListener() {
        sendCode()
    }

    override fun onValidateBtnListener(code: String): Boolean {
        return verifyCode(code)
    }

    private fun register(){
        val username = sign_detail_last_name_et.text.toString().trim()
        val email = sign_detail_email_et.text.toString().trim()
        val birthdate = sign_detail_birth_et.text.toString()
        val gender = getGender()

        if (validateRegisterDetail(username = username, email = email, birthdate = birthdate)){
            sessionManager.setCreateAccountUserDetail(
                username = username,
                email = email,
                birthdate = birthdate,
                gender = gender
            )
            viewModel.setStateEvent(
                RegisterAttemptEvent(
                    email = email,
                    gender = gender,
                    phone = phonenumber.toString(),
                    password = password.toString(),
                    first_name = username,
                    last_name = username,
                    birth_day = birthdate
                )
            )
        }
    }

    private fun validateRegisterDetail(username: String, email: String, birthdate: String): Boolean{
        if (username.isBlank()) {
            sign_detail_last_name_l_et.isErrorEnabled = true
            sign_detail_last_name_l_et.error = getString(R.string.invalid)
        }
        if (email.isBlank()) {
            sign_detail_email_l_et.isErrorEnabled = true
            sign_detail_email_l_et.error = getString(R.string.invalid)
        }
        if (birthdate.isBlank()) {
            sign_detail_birth_l_et.isErrorEnabled = true
            sign_detail_birth_l_et.error = getString(R.string.invalid)
        }
        if (username.isNotBlank() && email.isNotBlank() && birthdate.isNotBlank()) {
            sign_detail_last_name_l_et.isErrorEnabled = false
            sign_detail_email_l_et.isErrorEnabled = false
            sign_detail_birth_l_et.isErrorEnabled = false

            return true
        }
        return false
    }

    private fun sendCode(){
        viewModel.setStateEvent(
            SendCodeEvent(
                phonenumber.toString()
            )
        )
    }

    private fun verifyCode(code: String): Boolean{
        viewModel.setStateEvent(
            VerifyCodeEvent(
                phonenumber.toString(),
                code
            )
        )
        return true
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

    private fun showDatePicker(d: String){
        Locale.setDefault(Locale("ru"))

        var dateString = d
        if (dateString == ""){
            dateString = DateUtils.convertDateToString(
                DateUtils.getDateFromNYear(Constants.DEFAULT_YEAR_GAP)
            )
        }

        Log.e("DateSpinner", "$dateString")

        val date = Calendar.getInstance()
        date.time = DateUtils.convertStringToDate(dateString)
        val dd = date.get(Calendar.DAY_OF_MONTH)
        val mm = date.get(Calendar.MONTH)
        val yy = date.get(Calendar.YEAR)

        Log.e("DateSpinner", "$yy-$mm-$dd")

        val dpd = DatePickerDialog(requireContext(), R.style.MySpinnerDatePickerStyle,
            DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                sign_detail_birth_et.setText(
                    DateUtils.convertDateToString(
                        DateUtils.convertCalendarToDate(i,i2,i3)
                    )
                )
            }, yy, mm, dd)

        dpd.show()
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).text = getString(R.string.cancel_)
    }

    private fun getGender(): Int{
        if (sign_detail_gender_group.checkedRadioButtonId == sign_detail_man_btn.id) return 0
        return 1
    }
}
