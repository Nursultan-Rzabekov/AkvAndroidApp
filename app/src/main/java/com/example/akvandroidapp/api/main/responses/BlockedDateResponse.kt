package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BlockedDateResponse(
    @SerializedName("check_in")
    @Expose
    var check_in: String,

    @SerializedName("check_out")
    @Expose
    var check_out: String
)