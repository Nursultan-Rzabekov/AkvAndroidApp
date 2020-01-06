package com.example.akvandroidapp.session

data class SettingsInfo(
    var _pushNotificationsOn: Boolean = false,
    var _emailNotificationsOn: Boolean = false,
    var _region: String = "",
    var _geolocationState: Int = 0
)