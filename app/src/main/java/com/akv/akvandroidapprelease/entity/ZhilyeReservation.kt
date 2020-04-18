package com.akv.akvandroidapprelease.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "blog_zhilye_reservations")
data class ZhilyeReservation(

    @ColumnInfo(name = "check_in")
    var check_in: String? = null,

    @ColumnInfo(name = "check_out")
    var check_out: String? = null,

    @ColumnInfo(name = "user_id")
    var user_id: Int,

    @ColumnInfo(name = "first_name")
    var first_name: String? = null,

    @ColumnInfo(name = "last_name")
    var last_name: String? = null,

    @ColumnInfo(name = "userpic")
    var userpic: String? = null,

    @ColumnInfo(name = "email")
    var email: String? = null,

    @ColumnInfo(name = "income")
    var income: Int

): Parcelable