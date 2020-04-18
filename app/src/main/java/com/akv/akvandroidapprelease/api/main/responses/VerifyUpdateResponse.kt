package com.akv.akvandroidapprelease.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VerifyUpdateResponse(
    @SerializedName("response")
    @Expose
    var response: Boolean,

    @SerializedName("message")
    @Expose
    var message: String
)