package com.akv.akvandroidapprelease.util

import android.telephony.PhoneNumberUtils

class PhoneNumberUtil{
    companion object{
        fun formatStringToNumber(unformattedString: CharSequence?): String{
            return PhoneNumberUtils.formatNumber(unformattedString.toString(), "KZ")
        }
    }
}