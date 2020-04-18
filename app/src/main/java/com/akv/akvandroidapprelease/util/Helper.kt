package com.akv.akvandroidapprelease.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

object Helper {
    fun toastOnTop(
        context: Context?,
        text: String?,
        duration: Int,
        offset: Int
    ) {
        val toast = Toast.makeText(context, text, duration)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, offset)
        toast.setText(text)
        toast.duration = duration
        toast.show()
    }
}