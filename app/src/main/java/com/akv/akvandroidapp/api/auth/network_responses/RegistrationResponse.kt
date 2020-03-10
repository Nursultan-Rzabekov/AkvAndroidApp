package com.akv.akvandroidapp.api.auth.network_responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegistrationResponse(

    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("gender")
    @Expose
    var gender: Int? = null,

    @SerializedName("phone")
    @Expose
    var phone: String? = null,

    @SerializedName("first_name")
    @Expose
    var first_name: String? = null,

    @SerializedName("last_name")
    @Expose
    var last_name: String? = null,

    @SerializedName("birth_day")
    @Expose
    var birth_day: String? = null,

    @SerializedName("userpic")
    @Expose
    var userpic: String? = null,

    @SerializedName("is_active")
    @Expose
    var is_active: Boolean? = null,

    @SerializedName("is_phone_confirmed")
    @Expose
    var is_phone_confirmed: Boolean? = null,

    @SerializedName("user_type")
    @Expose
    var user_type: Int? = null,

    @SerializedName("error_message")
    @Expose
    var errorMessage: String? = null,

    @SerializedName("response")
    @Expose
    var response: Boolean? = null
)