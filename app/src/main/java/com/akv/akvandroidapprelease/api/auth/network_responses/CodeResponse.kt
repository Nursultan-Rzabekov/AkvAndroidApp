package com.akv.akvandroidapprelease.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CodeResponse(

    @SerializedName("response")
    @Expose
    var response: Boolean,

    @SerializedName("message")
    @Expose
    var message: String,

    @SerializedName("auth_token")
    @Expose
    var token: String,

    @SerializedName("user")
    @Expose
    var user: UserResponse

)
{
    override fun toString(): String {
        return "CodeResponse(response='$response', message='$message')"
    }
}
