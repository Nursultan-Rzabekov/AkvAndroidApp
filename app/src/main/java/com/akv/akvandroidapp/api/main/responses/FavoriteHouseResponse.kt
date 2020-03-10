package com.akv.akvandroidapp.api.main.responses


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavoriteHouseResponse(

    @SerializedName("house")
    @Expose
    var house: BlogSearchResponse

)