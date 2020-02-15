package com.example.akvandroidapp.ui.main.profile.add_ad.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import com.example.akvandroidapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class IbanRequestDialog(
    context: Context,
    var interaction: IbanRequestInteraction
): Dialog(context, R.style.CustomBasicDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0F)
        setContentView(R.layout.dialog_fill_iban)

        findViewById<ImageButton>(R.id.dialog_fill_iban_cancel_btn).setOnClickListener {
            Log.d("IbanRequestDialog", "IbanRequestDialog: cancelling.")
            interaction.onCloseBtnListener()
            dismiss()
        }

        findViewById<MaterialButton>(R.id.dialog_fill_iban_send_btn).setOnClickListener {

            Log.d("RejectReservationDialog", "RejectReservationDialog: send.")

            val editText = findViewById<TextInputEditText>(R.id.dialog_fill_iban_text_et)
            val editTextLayout = findViewById<TextInputLayout>(R.id.dialog_fill_iban_text_l_et)
            if (editText.text.toString().trim().isNotBlank() && editText.text.toString().trim().length == 16) {
                editTextLayout.isErrorEnabled = false
                interaction.onPostBtnListener(editText.text.toString().trim().toUpperCase(Locale.getDefault()))
            }
            else {
                editTextLayout.error = "Запольните поле"
                editTextLayout.isErrorEnabled = true
            }
            dismiss()
        }
    }

    interface IbanRequestInteraction {
        fun onCloseBtnListener()
        fun onCancelBtnListener()
        fun onPostBtnListener(iban: String)
    }
}