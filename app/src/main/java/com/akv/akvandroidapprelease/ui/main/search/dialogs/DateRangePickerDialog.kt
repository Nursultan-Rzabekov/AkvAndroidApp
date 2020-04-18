package com.akv.akvandroidapprelease.ui.main.search.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import com.akv.akvandroidapprelease.R
import com.google.android.material.button.MaterialButton
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.dialog_filter_dates.*
import java.util.*
import kotlin.collections.ArrayList

class DateRangePickerDialog(
    context: Context,
    var selectedDates: List<Date>,
    var blockedDates: List<Date>,
    val interaction: DatePickerDialogInteraction
): Dialog(context, R.style.CustomBasicDialog){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.5F)
        setContentView(R.layout.dialog_filter_dates)

        val lastyear = Calendar.getInstance()
        lastyear.add(Calendar.YEAR, 0)
        lastyear.add(Calendar.MONTH, 0)
        lastyear.add(Calendar.DAY_OF_MONTH, 0)

        dialog_filter_dates_picker.deactivateDates(ArrayList())

        val nextyear = Calendar.getInstance()
        nextyear.set(Calendar.YEAR, nextyear.get(Calendar.YEAR)+1)
        nextyear.set(Calendar.MONTH, Calendar.DECEMBER)
        nextyear.set(Calendar.DAY_OF_MONTH, 31)

        dialog_filter_dates_picker
            .init(lastyear.time, nextyear.time)
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withHighlightedDates(blockedDates)
            .withSelectedDates(selectedDates)

        findViewById<ImageButton>(R.id.dialog_filter_dates_picker_cancel).setOnClickListener {
            Log.d("FilterDialog", "FilterDialog: cancelling filter.")
            interaction.onCloseBtnListener()
            dismiss()
        }

        findViewById<MaterialButton>(R.id.dialog_filter_dates_save_btn).setOnClickListener {
            Log.d("FilterDialog", "FilterDialog: save filter.")
            Log.d("FilterDialog", "FilterDialog: ${dialog_filter_dates_picker.selectedDates}")
            if (dialog_filter_dates_picker.selectedDates.isNotEmpty()) {
                interaction.onSaveBtnListener(dialog_filter_dates_picker.selectedDates)
                dismiss()
            }else
                Toast.makeText(context, "Select dates", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.dialog_filter_dates_clear_all_btn).setOnClickListener {
            Log.d("FilterDialog", "FilterDialog: clear filter.")
            dialog_filter_dates_picker.clearSelectedDates()
            interaction.onClearBtnListener()
            Log.d("FilterDialog", "FilterDialog: ${dialog_filter_dates_picker.selectedDates}")
        }
    }

    interface DatePickerDialogInteraction{
        fun onCloseBtnListener()
        fun onClearBtnListener()
        fun onSaveBtnListener(dates: List<Date>)
    }
}