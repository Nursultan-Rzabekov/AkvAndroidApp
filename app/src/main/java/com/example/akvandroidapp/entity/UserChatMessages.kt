package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_post")
data class UserChatMessages(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "first_name")
    var first_name: String? = null,

    @ColumnInfo(name = "last_name")
    var last_name: String? = null,

    @ColumnInfo(name = "userpic")
    var userpic: String? = null

    ): Parcelable
