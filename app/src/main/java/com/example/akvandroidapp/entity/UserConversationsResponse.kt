package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserConversationsResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @ColumnInfo(name = "userId")
    var userId: Int? = null,

    @ColumnInfo(name = "userName")
    var userName: String? = null,

    @ColumnInfo(name = "userEmail")
    var userEmail: String? = null,

    @ColumnInfo(name = "userPic")
    var userPic: String? = null,

    @ColumnInfo(name = "recipientId")
    var recipientId: Int? = null,

    @ColumnInfo(name = "recipientName")
    var recipientName: String? = null,

    @ColumnInfo(name = "recipientEmail")
    var recipientEmail: String? = null,

    @ColumnInfo(name = "recipientPic")
    var recipientPic: String? = null,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("images")
    @Expose
    var images: String? = null,

    @SerializedName("created_at")
    @Expose
    var created_at: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updated_at: String? = null
):Parcelable