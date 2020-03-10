package com.akv.akvandroidapp.util

import android.telephony.PhoneNumberUtils

class PhoneNumberUtil{
    companion object{
        fun formatStringToNumber(unformattedString: CharSequence?): String{
            return PhoneNumberUtils.formatNumber(unformattedString.toString(), "KZ")
        }
    }
}