package com.example.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse(

//    @SerializedName("response")
//    @Expose
//    var response: String,

//    @SerializedName("error_message")
//    @Expose
//    var errorMessage: String,

    @SerializedName("auth_token")
    @Expose
    var auth_token: String

//    @SerializedName("pk")
//    @Expose
//    var pk: Int,

)
{
    override fun toString(): String {
        return "LoginResponse(auth_token='$auth_token')"
    }
}
