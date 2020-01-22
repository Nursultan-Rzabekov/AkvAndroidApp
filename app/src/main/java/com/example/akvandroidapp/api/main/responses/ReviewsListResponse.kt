package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewsListResponse(
    @SerializedName("count")
    @Expose
    var count: Int,

    @SerializedName("next")
    @Expose
    var next: String,

    @SerializedName("previous")
    @Expose
    var previous: String,

    @SerializedName("results")
    @Expose
    var results: List<ReviewResponse>
) {

    override fun toString(): String {
        return "ReviewsListResponse(results=$results, count='$count',next='$next',previous='$previous')"
    }
}