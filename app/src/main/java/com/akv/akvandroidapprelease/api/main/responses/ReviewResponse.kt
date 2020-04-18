package com.akv.akvandroidapprelease.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewResponse (
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("user")
    @Expose
    var user: UserProfileInfoResponse,

    @SerializedName("house")
    @Expose
    var house: Int,

    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("stars")
    @Expose
    var stars: Float,

    @SerializedName("created_at")
    @Expose
    var created_at: String
)