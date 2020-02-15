package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PaymentResultItemResponse (
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("amount")
    @Expose
    var amount: Int,

    @SerializedName("is_paid")
    @Expose
    var is_paid: Boolean,

    @SerializedName("payment_id")
    @Expose
    var payment_id: Long,

    @SerializedName("reservation_id")
    @Expose
    var reservation_id: Int
)