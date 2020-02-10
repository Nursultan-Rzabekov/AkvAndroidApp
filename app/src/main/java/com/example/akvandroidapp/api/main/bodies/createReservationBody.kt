package com.example.akvandroidapp.api.main.bodies

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateReservationBody(
    @SerializedName("check_in") @Expose var check_in: String,
    @SerializedName("check_out") @Expose var check_out: String,
    @SerializedName("guests") @Expose var guests: Int,
    @SerializedName("house_id") @Expose var house_id: Int
)