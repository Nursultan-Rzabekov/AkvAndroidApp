package com.akv.akvandroidapprelease.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConverstaionsResponse(

    @SerializedName("results")
    @Expose
    var results: List<UserConversationsInfoResponse>,

    @SerializedName("count")
    @Expose
    var count: Int,

    @SerializedName("next")
    @Expose
    var next: String,

    @SerializedName("previous")
    @Expose
    var previous: String

)