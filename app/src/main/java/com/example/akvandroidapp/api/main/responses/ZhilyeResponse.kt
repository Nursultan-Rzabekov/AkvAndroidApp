package com.example.akvandroidapp.api.main.responses

import com.example.akvandroidapp.api.auth.network_responses.UserResponse
import com.example.akvandroidapp.session.ProfileInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ZhilyeResponse(

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

    @SerializedName("price")
    @Expose
    var price: Int,

    @SerializedName("status")
    @Expose
    var status: Int,

    @SerializedName("beds")
    @Expose
    var beds: Int,

    @SerializedName("guests")
    @Expose
    var guests: Int,

    @SerializedName("rating")
    @Expose
    var rating: Double,

    @SerializedName("city")
    @Expose
    var city: String,

    @SerializedName("is_favourite")
    @Expose
    var is_favourite: Boolean,

    @SerializedName("discount7days")
    @Expose
    var discount7days: Int,

    @SerializedName("discount30days")
    @Expose
    var discount30days: Int,

    @SerializedName("photos")
    @Expose
    var photos: List<BlogImageZhilyeResponse>? = null,

    @SerializedName("accommodations")
    @Expose
    var accommodations: List<BlogPropertiesResponse>? = null,

    @SerializedName("near_buildings")
    @Expose
    var near_buildings: List<BlogPropertiesResponse>? = null,

    @SerializedName("rules")
    @Expose
    var rules: List<BlogPropertiesResponse>? = null,

    @SerializedName("user")
    @Expose
    var user: UserZhilyeResponse,

    @SerializedName("recommendations")
    @Expose
    var recommendations: List<BlogSearchResponse>
)