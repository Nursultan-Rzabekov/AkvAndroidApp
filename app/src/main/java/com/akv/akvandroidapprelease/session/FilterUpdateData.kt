package com.akv.akvandroidapprelease.session

import com.akv.akvandroidapprelease.util.Constants

data class FilterUpdateData(
    var setCityQuery : String = "нет",
    var setBlogFilterTypeHouse: Int = 0,
    var setBlogFilterPriceLeft: Int = 0,
    var setBlogFilterPriceRight: Int = 0,
    var setBlogFilterRoomsLeft: Int = 0,
    var setBlogFilterRoomsRight: Int = 0,
    var setBlogFilterBedsLeft: Int = 0,
    var setBlogFilterBedsRight: Int = 0,
    var setBlogOrdering: String = Constants.FILTER_TYPE1,
    var setBlogVerified: String = "false",
    var setBlogFilterAccomadations: String? = null
)