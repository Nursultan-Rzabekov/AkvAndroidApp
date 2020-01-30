package com.example.akvandroidapp.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs

class Converters {
    companion object {
        fun humanReadableByteCountSI(bytes: Long): String {
            val s = if (bytes < 0) "-" else ""
            var b = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
            if (b < 1000L)
                return "$bytes B"
            else if (b < 999_950L)
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} KB"
            else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} MB"
            } else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} GB"
            } else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} TB"
            } else if ((b / 1000) < 999_950L) {
                b /= 1000
                return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} PB"
            } else return "${BigDecimal(b/1e3).setScale(1, RoundingMode.HALF_EVEN)} PB"
        }


    }
}
class MoneyTextWatcher(
    editText: TextInputEditText
): TextWatcher{
    val weakReference = WeakReference<TextInputEditText>(editText)

    override fun afterTextChanged(p0: Editable?) {
        val editText = weakReference.get()
        if (editText == null) throw IllegalArgumentException("Money is not all")

        val str = p0.toString()
        if (str.isNotBlank()){
            val formatter = DecimalFormat("#,###")
            editText.removeTextChangedListener(this)
            editText.setText(formatter.format(str))
            editText.setSelection(str.length)
            editText.addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

}