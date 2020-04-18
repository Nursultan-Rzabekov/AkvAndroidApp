package com.akv.akvandroidapprelease.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class PayRequestResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("location")
    @Expose
    var location: String,

    @SerializedName("payment_page_url")
    @Expose
    var payment_page_url: String


): Parcelable