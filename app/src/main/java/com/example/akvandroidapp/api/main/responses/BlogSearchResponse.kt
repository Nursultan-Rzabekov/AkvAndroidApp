package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogSearchResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("city")
    @Expose
    var city: String,

    @SerializedName("longitude")
    @Expose
    var longitude: Double,

    @SerializedName("latitude")
    @Expose
    var latitude: Double,

    @SerializedName("house_type")
    @Expose
    var house_type: String,

    @SerializedName("price")
    @Expose
    var price: Int,

    @SerializedName("status")
    @Expose
    var status: Boolean,

    @SerializedName("beds")
    @Expose
    var beds: Int,

    @SerializedName("rooms")
    @Expose
    var rooms: Int,

    @SerializedName("rating")
    @Expose
    var rating: Double,

    @SerializedName("is_favourite")
    @Expose
    var is_favourite: Boolean,

    @SerializedName("photos")
    @Expose
    var photos: List<BlogImageResponse>? = null
)