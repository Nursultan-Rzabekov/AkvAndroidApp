package com.example.akvandroidapp.session

import java.util.*

data class HouseUpdateData(
    var id : Int = -1,
    var title: String? = null,
    var description: String? = null,
    var photosList: List<String>? = null,
    var houseRulesList: List<String>? = null,
    var facilitiesList: List<String>? = null,
    var nearByList: List<String>? = null,
    var availableDates: List<Date>? = null,
    var price: Int? = null,
    var address: Int? = null
)