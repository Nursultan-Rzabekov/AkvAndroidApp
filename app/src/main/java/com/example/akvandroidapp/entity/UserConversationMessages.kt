package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_conversation")
data class UserConversationMessages(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
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

    @ColumnInfo(name = "body")
    var body: String? = null,

    @ColumnInfo(name = "created_at")
    var created_at: String? = null,

    @ColumnInfo(name = "updated_at")
    var updated_at: String? = null,

    @ColumnInfo(name = "image")
    var image: String? = null

    ): Parcelable
