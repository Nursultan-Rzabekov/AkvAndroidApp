package com.akv.akvandroidapp.session

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class AddAdInfo(
    var _addAdType: String = "",
    var _addAdGuestsCount: Int = 0,
    var _addAdRoomsCount: Int = 0,
    var _addAdBedsCount: Int = 0,
    var _addAdAddress: String = "",
    var _addAdAddressRegionId: Int = -1,
    var _addAdAddressCityId: Int = -1,
    var _addAdAddressCountry: Int = -1,
    var _addAdAddressLatitude: Double = 0.0,
    var _addAdAddressLongitude: Double = 0.0,
    var _addAdDescription: String = "",
    var _addAdTitle: String = "",
    var _addAdPrice: Int = 0,
    var _addAd7DaysDiscount: Int = 0,
    var _addAd30DaysDiscount: Int = 0,
    var _addAdImage: MutableList<Uri> = mutableListOf(),
    var _addAdFacilityList: MutableList<String> = mutableListOf(),
    var _addAdNearByList: MutableList<String> = mutableListOf(),
    var _addAdRulesList: MutableList<String> = mutableListOf(),
    var _addAdAvailableList: MutableList<Date> = mutableListOf()
    ) : Parcelable