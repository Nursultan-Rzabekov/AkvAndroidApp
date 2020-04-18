package com.akv.akvandroidapprelease.api.main.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavoriteHouseResponse(

    @SerializedName("house")
    @Expose
    var house: BlogSearchResponse

)