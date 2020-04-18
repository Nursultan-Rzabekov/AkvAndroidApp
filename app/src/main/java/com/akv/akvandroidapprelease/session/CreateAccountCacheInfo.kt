package com.akv.akvandroidapprelease.session

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateAccountCacheInfo(
    var _username: String = "",
    var _phoneNumber: String = "",
    var birthDate: String = "",
    var _password: String = "",
    var _gender: Int = 1,
    var _email: String = ""
) : Parcelable