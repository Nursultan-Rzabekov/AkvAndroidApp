package com.akv.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "payment_history")
data class PaymentHistoryItem(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "amount")
    var amout: Int,

    @ColumnInfo(name = "is_paid")
    var is_paid: Boolean,

    @ColumnInfo(name = "payment_id")
    var payment_id: Long,

    @ColumnInfo(name = "reservation_id")
    var reservation_id: Int

): Parcelable