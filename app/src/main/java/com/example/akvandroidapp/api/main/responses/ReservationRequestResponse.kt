package com.example.akvandroidapp.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
class ReservationRequestResponse(

    @SerializedName("response")
    @Expose
    var response: Boolean

): Parcelable