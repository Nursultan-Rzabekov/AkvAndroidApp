package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_zhilye_photos")
data class ZhilyeDetailPhotos(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "house")
    var house: Int,

    @ColumnInfo(name = "image")
    var image: String? = null

    ): Parcelable