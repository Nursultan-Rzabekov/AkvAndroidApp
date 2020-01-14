package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AllChatsResponse(

    @SerializedName("results")
    @Expose
    var results: List<UserProfileInfoResponse>,

    @SerializedName("count")
    @Expose
    var count: Int,

    @SerializedName("next")
    @Expose
    var next: String,

    @SerializedName("previous")
    @Expose
    var previous: String

) {

}