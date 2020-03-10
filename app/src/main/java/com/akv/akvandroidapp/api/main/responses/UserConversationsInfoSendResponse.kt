package com.akv.akvandroidapp.api.main.responses

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserConversationsInfoSendResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("user")
    @Expose
    var user: Int,

    @SerializedName("recipient")
    @Expose
    var recipient: Int,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("images")
    @Expose
    var images: List<UserConversationsImagesResponse>? = null,

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null
):Parcelable