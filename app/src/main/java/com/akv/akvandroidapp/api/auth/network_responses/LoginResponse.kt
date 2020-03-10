package com.akv.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse(

    @SerializedName("non_field_errors")
    @Expose
    var errorMessage: List<String>? = null,

    @SerializedName("auth_token")
    @Expose
    var token: String,

    @SerializedName("user")
    @Expose
    var user: UserResponse

)
{
    override fun toString(): String {
        return "LoginResponse(errorMessage='$errorMessage', token='$token', user='$user')"
    }
}
