package com.akv.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyHouseStateResponse (

    @SerializedName("response")
    @Expose
    var response: Boolean,

    @SerializedName("message")
    @Expose
    var message: String? = null,

    @SerializedName("detail")
    @Expose
    var detail: String? = null

)