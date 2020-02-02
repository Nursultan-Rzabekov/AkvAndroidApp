package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyUpdateResponse(
    @SerializedName("detail")
    @Expose
    var detail: String? = null
)