package com.akv.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserZhilyeResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("first_name")
    @Expose
    var first_name: String,

    @SerializedName("last_name")
    @Expose
    var last_name: String,

    @SerializedName("userpic")
    @Expose
    var userpic: String,

    @SerializedName("email")
    @Expose
    var email: String
)