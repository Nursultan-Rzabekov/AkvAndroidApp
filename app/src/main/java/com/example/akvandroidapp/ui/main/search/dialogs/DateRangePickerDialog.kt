package com.example.akvandroidapp.ui.main.search.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.akvandroidapp.R
import com.google.android.material.button.MaterialButton
import com.savvi.rangedatepicker.CalendarPickerView
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.ArrayList

class DateRangePickerDialog(
    val interaction: DatePickerDialogInteraction
): DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_filter_dates, container, false)
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog =  activity?.let {
//            val builder = AlertDialog.Builder(it)
//            builder.setCancelable(false)
//
//            builder.setTitle("asdasd")
//
//            builder.create()
//        }?: throw IllegalStateException("Activity can not be null")
//
//        return dialog
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val closeBtn = view.findViewById<ImageButton>(R.id.dialog_filter_dates_picker_cancel)
        val clearAllBtn = view.findViewById<MaterialButton>(R.id.dialog_filter_dates_clear_all_btn)
        val saveBtn = view.findViewById<MaterialButton>(R.id.dialog_filter_dates_save_btn)
        val calendarPicker = view.findViewById<CalendarPickerView>(R.id.dialog_filter_dates_picker)

        val lastyear = Calendar.getInstance()
        lastyear.add(Calendar.YEAR, 0)
        lastyear.add(Calendar.MONTH, 0)
        lastyear.add(Calendar.DAY_OF_MONTH, 0)

        calendarPicker.deactivateDates(ArrayList())

        val nextyear = Calendar.getInstance()
        nextyear.set(Calendar.YEAR, nextyear.get(Calendar.YEAR)+1)
        nextyear.set(Calendar.MONTH, Calendar.DECEMBER)
        nextyear.set(Calendar.DAY_OF_MONTH, 31)

        calendarPicker
            .init(lastyear.time, nextyear.time)
            .inMode(CalendarPickerView.SelectionMode.RANGE)

        closeBtn.setOnClickListener {
            interaction.onCloseBtnListener()
            dismiss()
        }
        clearAllBtn.setOnClickListener {
            interaction.onClearBtnListener()
        }
        saveBtn.setOnClickListener {
            interaction.onSaveBtnListener()
        }
    }

    interface DatePickerDialogInteraction{
        fun onCloseBtnListener()
        fun onClearBtnListener()
        fun onSaveBtnListener()
    }
}