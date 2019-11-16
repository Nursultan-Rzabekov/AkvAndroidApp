package com.example.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationResponse(


    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("phone")
    @Expose
    var phone: String,

    @SerializedName("first_name")
    @Expose
    var first_name: String,

    @SerializedName("last_name")
    @Expose
    var last_name: String,

    @SerializedName("birth_day")
    @Expose
    var birth_day: String

//    @SerializedName("error_message")
//    @Expose
//    var errorMessage: String

    )
{

    override fun toString(): String {
        return "RegistrationResponse(response='$id', email='$email', username='$username', phone='$phone', first_name='$first_name',last_name='$last_name',birth_day='$birth_day')"
    }
}