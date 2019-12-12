package com.example.akvandroidapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "blog_post")
data class BlogPost(

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
    var house_type: Int,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "status")
    var status: Int,

//    @ColumnInfo(name = "photos")
//    var photos: String,

    @ColumnInfo(name = "rating")
    var rating: Double

    ) {

    override fun toString(): String {
        return "BlogPost(id=$id, " +
                "name='$name', " +
                "description='$description', " +
                "rooms='$rooms', " +
                "floor='$floor', " +
                "address='$address', " +
                "longitude='$longitude', " +
                "latitude='$latitude', " +
                "house_type='$house_type', " +
                "price='$price', " +
                "status='$status', " +
                "rating='$rating')"
    }

}
