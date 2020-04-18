package com.akv.akvandroidapprelease.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class ReservationRequestResponse(

    @SerializedName("check_in")
    @Expose
    var check_in: String

): Parcelable