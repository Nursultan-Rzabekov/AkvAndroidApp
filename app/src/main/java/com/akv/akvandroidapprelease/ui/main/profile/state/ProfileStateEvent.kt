package com.akv.akvandroidapprelease.ui.main.profile.state


import okhttp3.MultipartBody


sealed class ProfileStateEvent {
    object GetProfileInfoEvent: ProfileStateEvent()

    data class EditProfileInfoEvent(
        var phone: String? = null,
        var birth_day: String? = null,
        var gender: Int? = null,
        var email: String? = null,
        var iban: String? = null,
        val image: MultipartBody.Part? = null

    ): ProfileStateEvent()

    data class SendCodeEvent(
        val phone: String
    ): ProfileStateEvent()

    data class VerifyCodeEvent(
        val phone: String,
        val code:String
    ):ProfileStateEvent()

    object None: ProfileStateEvent()
}