package com.example.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("gender")
    @Expose
    var gender: Int,

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
) {
    override fun toString(): String {
        return "RegistrationResponse(id='$id', email='$email', gender='$gender', phone='$phone', first_name='$first_name',last_name='$last_name',birth_day='$birth_day')"
    }

}