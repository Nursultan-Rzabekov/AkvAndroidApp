package com.akv.akvandroidapp.ui.auth.state

sealed class AuthStateEvent{

    data class LoginAttemptEvent(
        val email: String,
        val password: String
    ): AuthStateEvent()

    data class RegisterAttemptEvent(
        val email: String,
        val gender: Int,
        val phone: String,
        val password: String,
        val first_name: String,
        val last_name: String,
        val birth_day: String

    ): AuthStateEvent()


    data class SendCodeEvent(
        val phone: String
    ): AuthStateEvent()

    data class ForgetCodeEvent(
        val email: String
    ): AuthStateEvent()

    data class VerifyCodeEvent(
        val phone: String,
        val code:String
    ):AuthStateEvent()

    object CheckPreviousAuthEvent : AuthStateEvent()

    object None: AuthStateEvent()
}