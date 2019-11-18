package com.example.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse(

    @SerializedName("response")
    @Expose
    var response: Boolean,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("user")
    @Expose
    var user: UserResponse

)
{
    override fun toString(): String {
        return "LoginResponse(response='$response', errorMessage='$errorMessage', token='$token', user='$user')"
    }
}
