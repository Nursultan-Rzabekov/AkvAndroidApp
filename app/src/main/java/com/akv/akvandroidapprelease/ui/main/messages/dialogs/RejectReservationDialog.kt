package com.akv.akvandroidapprelease.ui.main.messages.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import com.akv.akvandroidapprelease.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RejectReservationDialog(
    context: Context,
    var itemId: Int,
    val interaction: RejectReservationInteraction
): Dialog(context, R.style.CustomBasicDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.5F)
        setContentView(R.layout.dialog_request_deny)

        findViewById<ImageButton>(R.id.dialog_request_deny_cancel_btn).setOnClickListener {
            Log.d("RejectReservationDialog", "RejectReservationDialog: cancelling.")
            interaction.onCloseBtnListener()
            dismiss()
        }

        findViewById<TextView>(R.id.dialog_request_deny_skip_tv).setOnClickListener {
            Log.d("RejectReservationDialog", "RejectReservationDialog: skip.")
            interaction.onSkipBtnListener(itemId)
            dismiss()
        }

        findViewById<MaterialButton>(R.id.dialog_request_deny_send_btn).setOnClickListener {
            Log.d("RejectReservationDialog", "RejectReservationDialog: send.")

            val editText = findViewById<TextInputEditText>(R.id.dialog_request_deny_text_et)
            val editTextLayout = findViewById<TextInputLayout>(R.id.dialog_request_deny_text_l_et)
            if (editText.text.toString().trim().isNotBlank()) {
                editTextLayout.isErrorEnabled = false
                interaction.onSendBtnListener(itemId, editText.text.toString().trim())
            }
            else {
                editTextLayout.error = "Запольните поле"
                editTextLayout.isErrorEnabled = true
            }
            dismiss()
        }
    }

    interface RejectReservationInteraction{
        fun onCloseBtnListener()
        fun onSkipBtnListener(itemId: Int)
        fun onSendBtnListener(itemId: Int, message: String)
    }
}