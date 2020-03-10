package com.akv.akvandroidapp.session

import android.net.Uri

data class ProfileInfo(
    var nickname: String? = null,
    var birthdate: String? = null,
    var gender: Int? = null,
    var iban: String? = null,
    var phonenumber: String? = null,
    var email: String? = null,
    var image: Uri? = null,
    var imageBackend: String? = null
)