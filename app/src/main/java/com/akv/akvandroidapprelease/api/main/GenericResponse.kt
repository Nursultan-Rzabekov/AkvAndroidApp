package com.akv.akvandroidapprelease.api.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenericResponse(
    @SerializedName("response")
    @Expose
    var response: Boolean
)