package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_conversation__")
data class UserConversationSendMessages(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "userId")
    var userId: Int? = null,

    @ColumnInfo(name = "recipientId")
    var recipientId: Int? = null,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "body")
    var body: String? = null,

    @ColumnInfo(name = "created_at")
    var created_at: String? = null,

    @ColumnInfo(name = "updated_at")
    var updated_at: String? = null

    ): Parcelable
