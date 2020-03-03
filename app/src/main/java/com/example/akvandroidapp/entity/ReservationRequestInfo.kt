package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "reservation_requests")
data class ReservationRequestInfo(
    @ColumnInfo(name = "check_in")
    var check_in: String
): Parcelable