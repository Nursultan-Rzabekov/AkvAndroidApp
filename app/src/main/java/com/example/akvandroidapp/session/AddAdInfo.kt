package com.example.akvandroidapp.session

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AddAdInfo(
    var _addAdType: String = "",
    var _addAdGuestsCount: Int = 0,
    var _addAdRoomsCount: Int = 0,
    var _addAdBedsCount: Int = 0,
    var _addAdAddressList: MutableList<String> = mutableListOf("", "", "", "", ""),
    var _addAdDescription: String = "",
    var _addAdTitle: String = "",
    var _addAdPrice: Int = 0,
    var _addAd7DaysDiscount: Int = 0,
    var _addAd30DaysDiscount: Int = 0,
    var _addAdImage: MutableList<Uri> = mutableListOf()
    ) : Parcelable