package com.example.akvandroidapp.api.main.responses

import com.example.akvandroidapp.api.auth.network_responses.UserResponse
import com.example.akvandroidapp.entity.AccountProperties
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogSearchResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("rooms")
    @Expose
    var rooms: Int,

    @SerializedName("floor")
    @Expose
    var floor: Int,

    @SerializedName("address")
    @Expose
    var address: String,

    @SerializedName("longitude")
    @Expose
    var longitude: Double,

    @SerializedName("latitude")
    @Expose
    var latitude: Double,

    @SerializedName("house_type")
    @Expose
    var house_type: String,

    @SerializedName("city")
    @Expose
    var city: String,

    @SerializedName("price")
    @Expose
    var price: Int,

    @SerializedName("status")
    @Expose
    var status: Int,

    @SerializedName("photos")
    @Expose
    var photos: List<BlogImageResponse>,

    @SerializedName("houseaccoms")
    @Expose
    var houseaccoms: List<Int>,

//    @SerializedName("houserules")
//    @Expose
//    var houserules: List<Int>,

    @SerializedName("user")
    @Expose
    var user: UserResponse,

    @SerializedName("rating")
    @Expose
    var rating: Double


) {
    override fun toString(): String {
        return "BlogSearchResponse(id=$id, " +
                "name='$name', " +
                "description='$description', " +
                "rooms='$rooms'," +
                "floor='$floor'," +
                "address='$address', " +
                "longitude='$longitude', " +
                "latitude='$latitude', " +
                "house_type='$house_type', " +
                "price='$price', " +
                "status='$status', " +
//                "photos='$photos', " +
                "houseaccoms='$houseaccoms', " +
                "city='$city', " +
                "user='$user', " +
                "rating='$rating')"
    }
}