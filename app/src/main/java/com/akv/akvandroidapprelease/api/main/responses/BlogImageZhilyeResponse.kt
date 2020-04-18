package com.akv.akvandroidapprelease.api.main.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogImageZhilyeResponse(
    @SerializedName("house")
    @Expose
    var house: Int,

    @SerializedName("image")
    @Expose
    var image: String
)