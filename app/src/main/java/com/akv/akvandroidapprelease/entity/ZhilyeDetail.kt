package com.akv.akvandroidapprelease.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_zhilye_details")
data class ZhilyeDetail(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "rooms")
    var rooms: Int,

    @ColumnInfo(name = "floor")
    var floor: Int,

    @ColumnInfo(name = "address")
    var address: String? = null,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "house_type")
    var house_type: String? = null,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "status")
    var status: Boolean,

    @ColumnInfo(name = "beds")
    var beds: Int,

    @ColumnInfo(name = "guests")
    var guests: Int,

    @ColumnInfo(name = "rating")
    var rating: Double,

    @ColumnInfo(name = "city")
    var city: String? = null,

    @ColumnInfo(name = "is_favourite")
    var is_favourite: Boolean = false,

    @ColumnInfo(name = "is_owner")
    var is_owner: Boolean = false,

    @ColumnInfo(name = "discount7days")
    var discount7days: Int,

    @ColumnInfo(name = "discount30days")
    var discount30days: Int


    ): Parcelable
