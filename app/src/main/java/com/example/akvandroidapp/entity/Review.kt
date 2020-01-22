package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "reviews")
data class Review (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "house")
    var house: Int? = null,

    @ColumnInfo(name = "body")
    var body: String? = null,

    @ColumnInfo(name = "stars")
    var stars: Float? = null,

    @ColumnInfo(name = "created_at")
    var created_at: String? = null,

    @ColumnInfo(name = "user_id")
    var user_id: Int? = null,

    @ColumnInfo(name = "first_name")
    var first_name: String? = null,

    @ColumnInfo(name = "last_name")
    var last_name: String? = null,

    @ColumnInfo(name = "userpic")
    var userpic: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null

): Parcelable