package com.akv.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "reservations")
data class HomeReservation (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "check_in")
    var check_in: String? = null,

    @ColumnInfo(name = "check_out")
    var check_out: String? = null,

    @ColumnInfo(name = "guests")
    var guests: Int? = null,

    @ColumnInfo(name = "status")
    var status: Int? = null,

    @ColumnInfo(name = "status_name")
    var status_name: String? = null,

    @ColumnInfo(name = "created_at")
    var created_at: String? = null,

    @ColumnInfo(name = "user_id")
    var user_id: Int? = null,

    @ColumnInfo(name = "house_id")
    var house_id: Int? = null,

    @ColumnInfo(name = "house_name")
    var house_name: String? = null,

    @ColumnInfo(name = "house_image")
    var house_image: String? = null,

    @ColumnInfo(name = "owner_id")
    var owner_id: Int? = null,

    @ColumnInfo(name = "message")
    var message: String? = null

): Parcelable