package com.example.akvandroidapp.ui.auth.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.broooapps.otpedittext2.OtpEditText
import com.example.akvandroidapp.R
import com.google.android.material.button.MaterialButton

class CodeValidationDialog(
    context: Context,
    val interaction: CodeValidationDialogInteraction
): Dialog(context, R.style.CustomBasicDialog) {

    private val countdown = 60L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.5F)
        setContentView(R.layout.dialog_sign_up_valid)

        val body = findViewById<OtpEditText>(R.id.dialog_sign_up_valid_code_et)
        val close = findViewById<ImageButton>(R.id.dialog_sign_up_valid_close_btn)
        val send = findViewById<TextView>(R.id.dialog_sign_up_valid_repeat_code_tv)

        close.setOnClickListener {
            interaction.onCloseBtnListener()
            dismiss()
        }

        send.setOnClickListener {
            startCountDown(countdown)
            interaction.onSendMoreBtnListener()
        }

        body.setOnCompleteListener {
            if (interaction.onValidateBtnListener(it))
                dismiss()
        }

        startCountDown(countdown)
    }

    private fun startCountDown(time: Long){
        val timerTv = findViewById<TextView>(R.id.dialog_sign_up_valid_countdown_tv)
        val send = findViewById<TextView>(R.id.dialog_sign_up_valid_repeat_code_tv)

        timerTv.visibility = View.VISIBLE
        send.visibility = View.GONE

        val cTimer = object: CountDownTimer(time * 1000, 1000){
            override fun onFinish() {
                timerTv.visibility = View.GONE
                send.visibility = View.VISIBLE
            }

            override fun onTick(millisUntilFinished: Long) {
                timerTv.text = ("${millisUntilFinished/1000} сек.")
            }
        }
        cTimer.start()
    }

    interface CodeValidationDialogInteraction{
        fun onCloseBtnListener()
        fun onValidateBtnListener(code: String): Boolean
        fun onSendMoreBtnListener()
    }

}