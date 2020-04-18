package com.akv.akvandroidapprelease.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FeedbackSendResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("message")
    @Expose
    var message: String,

    @SerializedName("created_at")
    @Expose
    var created_at: String,

    @SerializedName("updated_at")
    @Expose
    var updated_at: String,

    @SerializedName("user")
    @Expose
    var user: UserZhilyeResponse

) {
}