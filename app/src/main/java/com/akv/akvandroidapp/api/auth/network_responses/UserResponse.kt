package com.akv.akvandroidapp.api.auth.network_responses

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

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("birth_day")
    @Expose
    var birth_day: String
) {
    override fun toString(): String {
        return "UserResponse(id='$id', email='$email', gender='$gender', phone='$phone', name='$name',birth_day='$birth_day')"
    }

}