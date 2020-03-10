package com.akv.akvandroidapp.api.main.responses


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MessageResponse(

    @SerializedName("user")
    @Expose
    var user: List<UserProfileInfoResponse>,

    @SerializedName("message")
    @Expose
    var message: String
):Parcelable