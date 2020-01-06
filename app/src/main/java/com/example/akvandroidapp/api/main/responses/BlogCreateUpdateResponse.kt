package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogCreateUpdateResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String

//    @SerializedName("description")
//    @Expose
//    var description: String,
//
//    @SerializedName("rooms")
//    @Expose
//    var rooms: Int,
//
//    @SerializedName("floor")
//    @Expose
//    var floor: Int,
//
//    @SerializedName("address")
//    @Expose
//    var address: String,
//
//    @SerializedName("longitude")
//    @Expose
//    var longitude: Double,
//
//    @SerializedName("latitude")
//    @Expose
//    var latitude: Double,
//
//    @SerializedName("price")
//    @Expose
//    var price: Int,
//
//    @SerializedName("beds")
//    @Expose
//    var beds: Int,
//
//    @SerializedName("guests")
//    @Expose
//    var guests: Int

)