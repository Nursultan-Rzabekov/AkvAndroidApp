package com.akv.akvandroidapprelease.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_conversation_images")
data class UserConversationImages(

    @PrimaryKey
    @ColumnInfo(name = "message")
    var message: Int,

    @ColumnInfo(name = "image")
    var image: String? = null

): Parcelable
