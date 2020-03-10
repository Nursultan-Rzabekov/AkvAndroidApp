package com.akv.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ZhilyeUserReservationResponse(
    @SerializedName("check_in")
    @Expose
    var check_in: String,

    @SerializedName("check_out")
    @Expose
    var check_out: String,

    @SerializedName("user")
    @Expose
    var user: UserZhilyeResponse,

    @SerializedName("income")
    @Expose
    var income: Int
)