package com.example.akvandroidapp.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.textfield.TextInputEditText
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

        fun formatPriceToInt(str: String): Int {
            if (str.isBlank()) return 0
            var newStr = str

            if(str.contains(","))
                newStr = str.replace(",","")
            if(str.contains(" "))
                newStr = str.replace(" ","")
            return newStr.toInt()
        }

        fun pretifyPrice(price: Int): String {
            val formatter = DecimalFormat("#,###")
            return formatter.format(price)
        }

    }
}
class MoneyTextWatcher(
    editText: TextInputEditText
): TextWatcher{
    val weakReference = WeakReference<TextInputEditText>(editText)

    override fun afterTextChanged(p0: Editable?) {
        val editText = weakReference.get() ?: throw IllegalArgumentException("Money is not all")

        editText.removeTextChangedListener(this)

        try {
            var str = p0.toString()
            if (str.isNotEmpty()) {
                if (str.contains(","))
                    str = str.replace(",", "")
                if (str.contains(" "))
                    str = str.replace(" ", "")
                val long = str.toLong()

                Log.e("MoneyTextWatcher", str)

                val formatter = DecimalFormat("#,###,###")
                editText.setText(formatter.format(long))
                editText.setSelection(editText.text.toString().length)
            } else {
                editText.setText("0")
                editText.setSelection(editText.text.toString().length)
            }
        }catch (e: Exception){
            editText.setText("0")
            editText.setSelection(editText.text.toString().length)
            Log.e("PriceWatcher", e.message.toString())
        }


        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

}