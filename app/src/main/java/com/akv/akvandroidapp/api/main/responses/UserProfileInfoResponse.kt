package com.akv.akvandroidapp.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserProfileInfoResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("first_name")
    @Expose
    var first_name: String,

    @SerializedName("last_name")
    @Expose
    var last_name: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("userpic")
    @Expose
    var userpic: String
):Parcelable