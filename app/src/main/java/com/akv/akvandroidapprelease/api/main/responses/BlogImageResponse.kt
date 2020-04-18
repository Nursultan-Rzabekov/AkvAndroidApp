package com.akv.akvandroidapprelease.api.main.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogImageResponse(

    @SerializedName("house")
    @Expose
    var house: Int,

    @SerializedName("image")
    @Expose
    var image: String
) {
    override fun toString(): String {
        return "BlogImageResponse(image=$image)"
    }
}