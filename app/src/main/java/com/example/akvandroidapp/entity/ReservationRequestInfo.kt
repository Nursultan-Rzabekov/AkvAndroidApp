package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "reservation_requests")
data class ReservationRequestInfo(
    @ColumnInfo(name = "response")
    var response: Boolean
): Parcelable