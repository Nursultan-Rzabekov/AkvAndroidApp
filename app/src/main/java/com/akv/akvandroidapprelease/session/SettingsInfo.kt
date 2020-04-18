package com.akv.akvandroidapprelease.session

import com.akv.akvandroidapprelease.ui.main.search.filter.FilterCity

data class SettingsInfo(
    var _pushNotificationsOn: Boolean = false,
    var _emailNotificationsOn: Boolean = false,
    var _region: FilterCity = FilterCity("нет", false, true, -1),
    var _geolocationState: Int = 0
)