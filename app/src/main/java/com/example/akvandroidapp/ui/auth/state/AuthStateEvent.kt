package com.example.akvandroidapp.ui.auth.state

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

    class CheckPreviousAuthEvent(): AuthStateEvent()

    class None: AuthStateEvent()
}