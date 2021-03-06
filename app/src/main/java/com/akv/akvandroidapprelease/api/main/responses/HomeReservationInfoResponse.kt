package com.akv.akvandroidapprelease.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeReservationInfoResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("check_in")
    @Expose
    var check_in: String,

    @SerializedName("check_out")
    @Expose
    var check_out: String,

    @SerializedName("days")
    @Expose
    var days: Int,

    @SerializedName("guests")
    @Expose
    var guests: Int,

    @SerializedName("status")
    @Expose
    var status: Int,

    @SerializedName("status_name")
    @Expose
    var status_name: String,

    @SerializedName("created_at")
    @Expose
    var created_at: String,

    @SerializedName("user")
    @Expose
    var user: UserProfileInfoResponse,

    @SerializedName("house")
    @Expose
    var house: MyHouseInfoResponse,

    @SerializedName("owner")
    @Expose
    var owner: UserProfileInfoResponse,

    @SerializedName("message")
    @Expose
    var message: String

)