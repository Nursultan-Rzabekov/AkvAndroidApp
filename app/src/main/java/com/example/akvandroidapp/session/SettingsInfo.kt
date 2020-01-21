package com.example.akvandroidapp.session

import com.example.akvandroidapp.ui.main.search.filter.FilterCity

data class SettingsInfo(
    var _pushNotificationsOn: Boolean = false,
    var _emailNotificationsOn: Boolean = false,
    var _region: FilterCity = FilterCity("нет", false, true, -1),
    var _geolocationState: Int = 0
)