package com.akv.akvandroidapprelease.api.main.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogPropertiesResponse(
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @Expose
    var name: String
)