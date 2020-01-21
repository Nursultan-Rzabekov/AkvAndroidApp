package com.example.akvandroidapp.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserConversationsInfoResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("user")
    @Expose
    var user: String,

    @SerializedName("recipient")
    @Expose
    var recipient: String,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("images")
    @Expose
    var images: List<UserConversationsImagesResponse>? = null,

    @SerializedName("created_at")
    @Expose
    var created_at: String,

    @SerializedName("updated_at")
    @Expose
    var updated_at: String
):Parcelable