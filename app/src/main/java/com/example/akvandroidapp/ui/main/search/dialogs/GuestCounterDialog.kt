package com.example.akvandroidapp.ui.main.search.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.search.viewmodel.clearCounts
import com.example.akvandroidapp.ui.main.search.viewmodel.setAdultCount
import com.example.akvandroidapp.ui.main.search.viewmodel.setChildrenCount
import com.google.android.material.button.MaterialButton

class GuestCounterDialog(
    context: Context,
    var adultsCount: Int,
    var childrenCount: Int,
    val interaction: GuestCounterDialogInteraction
): Dialog(context, R.style.CustomBasicDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.5F)
        setContentView(R.layout.dialog_guests)

        val adults = findViewById<TextView>(R.id.dialog_guests_adult_tv)
        val children = findViewById<TextView>(R.id.dialog_guests_children_tv)

        adults.text = adultsCount.toString()
        children.text = childrenCount.toString()

        findViewById<ImageButton>(R.id.dialog_date_cancel).setOnClickListener {
            Log.d("GuestCounterDialog", "FilterDialog: cancelling filter.")
            interaction.onCloseBtnListenerCounter()
            dismiss()
        }

        findViewById<MaterialButton>(R.id.dialog_guests_save_btn).setOnClickListener {
            Log.d("GuestCounterDialog", "FilterDialog: save filter.")
            if (adultsCount != 0 && adultsCount != 0) {
                interaction.onSaveBtnListenerCounter(adultsCount, childrenCount)
                dismiss()
            }else
                Toast.makeText(context, "Select guests", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.dialog_guests_clear_all_btn).setOnClickListener {
            interaction.onClearBtnListenerCounter()
            adultsCount = 0
            childrenCount = 0
            adults.text = adultsCount.toString()
            children.text = childrenCount.toString()
        }

        findViewById<ImageButton>(R.id.dialog_guests_adult_minus_btn).setOnClickListener {
            if (adultsCount > 1)
                adultsCount -= 1
            adults.text = adultsCount.toString()
        }

        findViewById<ImageButton>(R.id.dialog_guests_adult_plus_btn).setOnClickListener {
            adultsCount += 1
            adults.text = adultsCount.toString()
        }

        findViewById<ImageButton>(R.id.dialog_guests_children_minus_btn).setOnClickListener {
            if (childrenCount > 0)
                childrenCount -= 1
            children.text = childrenCount.toString()
        }

        findViewById<ImageButton>(R.id.dialog_guests_children_plus_btn).setOnClickListener {
            if (adultsCount == 0)
                adultsCount += 1
            childrenCount += 1
            children.text = childrenCount.toString()
            adults.text = adultsCount.toString()
        }
    }

    interface GuestCounterDialogInteraction{
        fun onCloseBtnListenerCounter()
        fun onClearBtnListenerCounter()
        fun onSaveBtnListenerCounter(adultsCount: Int, childrenCount: Int)
    }
}