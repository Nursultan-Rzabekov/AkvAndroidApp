package com.akv.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "blog_zhilye_blocked_date")
data class ZhilyeBlockedDate(

    @ColumnInfo(name = "check_in")
    var check_in: String? = null,

    @ColumnInfo(name = "check_out")
    var check_out: String? = null

): Parcelable