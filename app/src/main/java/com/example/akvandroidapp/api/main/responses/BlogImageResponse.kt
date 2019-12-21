package com.example.akvandroidapp.api.main.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogImageResponse(

    @SerializedName("image")
    @Expose
    var image: String
) {
    override fun toString(): String {
        return "BlogImageResponse(image=$image)"
    }
}