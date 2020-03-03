package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_pay_response_details")
data class PayReservationIdResponse(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "location")
    var location: String? = null,

    @ColumnInfo(name = "payment_page_url")
    var payment_page_url: String? = null

    ): Parcelable
