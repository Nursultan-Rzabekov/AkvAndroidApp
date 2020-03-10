package com.akv.akvandroidapp.api.main.bodies

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateCancelReservationBody(
    @SerializedName("message") @Expose var message: String
)