package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlogGetProfileInfoResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("phone")
    @Expose
    var phone: String,

    @SerializedName("birth_day")
    @Expose
    var birth_day: String,

    @SerializedName("gender")
    @Expose
    var gender: Int,

    @SerializedName("first_name")
    @Expose
    var first_name: String,

    @SerializedName("last_name")
    @Expose
    var last_name: String,

    @SerializedName("email")
    @Expose
    var email: String,

    @SerializedName("userpic")
    @Expose
    var userpic: String,

    @SerializedName("is_active")
    @Expose
    var is_active: Boolean,

    @SerializedName("iban")
    @Expose
    var iban: String,

    @SerializedName("is_phone_confirmed")
    @Expose
    var is_phone_confirmed: Boolean
)