package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BlogListFavoritesResponse(

    @SerializedName("results")
    @Expose
    var results: List<FavoriteHouseResponse>,

    @SerializedName("count")
    @Expose
    var count: Int,

    @SerializedName("next")
    @Expose
    var next: String,

    @SerializedName("previous")
    @Expose
    var previous: String

)