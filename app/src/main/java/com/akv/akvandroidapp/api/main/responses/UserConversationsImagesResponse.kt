package com.akv.akvandroidapp.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserConversationsImagesResponse(

    @SerializedName("message")
    @Expose
    var message: Int,

    @SerializedName("image")
    @Expose
    var image: String

):Parcelable