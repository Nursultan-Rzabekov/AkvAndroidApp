package com.example.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationResponse(

    @SerializedName("email")
    @Expose
    var email: List<String>? = null,

    @SerializedName("token")
    @Expose
    var token: String,

    @SerializedName("user")
    @Expose
    var user: UserResponse,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String

    )
{
    override fun toString(): String {
        return "RegistrationResponse(errorMessage='$errorMessage', token='$token', user='$user')"
    }
}