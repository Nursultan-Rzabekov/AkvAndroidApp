package com.example.akvandroidapp.ui.main.profile.state


import okhttp3.MultipartBody


sealed class ProfileStateEvent {
    class GetProfileInfoEvent: ProfileStateEvent()

    data class EditProfileInfoEvent(
        var phone: String? = null,
        var birth_day: String? = null,
        var gender: Int? = null,
//        var first_name: String? = null,
//        var last_name: String? = null,
        var email: String? = null,
        val image: MultipartBody.Part? = null
    ): ProfileStateEvent()



    class None: ProfileStateEvent()
}