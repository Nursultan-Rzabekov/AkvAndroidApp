package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_post")
data class BlogPost(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "beds")
    var beds: Int = 0,

    @ColumnInfo(name = "rooms")
    var rooms: Int,

    @ColumnInfo(name = "is_favourite")
    var is_favourite: Boolean = false,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "house_type")
    var house_type: String? = null,

    @ColumnInfo(name = "city")
    var city: String? = null,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "status")
    var status: Int,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "rating")
    var rating: Double

    ): Parcelable  {

    override fun toString(): String {
        return "BlogPost(id=$id, " +
                "name='$name', " +
                "beds='$beds', " +
                "rooms='$rooms', " +
                "is_favourite='$is_favourite', " +
                "longitude='$longitude', " +
                "latitude='$latitude', " +
                "house_type='$house_type', " +
                "city='$city', " +
                "price='$price', " +
                "status='$status', " +
                "image='$image', " +
                "rating='$rating')"
    }
}
