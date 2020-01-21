package com.example.akvandroidapp.api.main.bodies

import com.google.gson.annotations.SerializedName

data class CreateReservationBody(
    @SerializedName("check_in") var check_in: String,
    @SerializedName("check_out") var check_out: String,
    @SerializedName("guests") var guests: Int,
    @SerializedName("house_id") var house_id: Int
)