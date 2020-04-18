package com.akv.akvandroidapprelease.ui.auth.state

import com.akv.akvandroidapprelease.entity.AccountProperties
import com.akv.akvandroidapprelease.entity.AuthToken


data class AuthViewState(
    var registrationFields: RegistrationFields? = RegistrationFields(),
    var loginFields: LoginFields? = LoginFields(),
    var sendCodeFields: SendCodeFields? = SendCodeFields(),
    var verifyCodeFields: VerifyCodeFields? = VerifyCodeFields(),
    var authToken: AuthToken? = null,
    var responseVerify: Boolean = false,
    var authViewStateResponse: AuthViewStateResponse? = AuthViewStateResponse()
)

data class AuthViewStateResponse(
    var state: AccountProperties? = null,
    var isQueryInProgress: Boolean = false,
    var isQueryExhausted: Boolean = false
)

data class RegistrationFields(
    var registration_email: String? = null,
    var registration_username: String? = null,
    var registration_birth_day: String? = null
){
    class RegistrationError {
        companion object{

            fun mustFillAllFields(): String{
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String{
                return "Passwords must match."
            }

            fun none():String{
                return "None"
            }

        }
    }

    fun isValidForRegistration(): String{
        if(registration_email.isNullOrEmpty()
            || registration_username.isNullOrEmpty()
            || registration_birth_day.isNullOrEmpty()){
            return RegistrationError.mustFillAllFields()
        }

        return RegistrationError.none()
    }
}

data class LoginFields(
    var login_email: String? = null,
    var login_password: String? = null
){
    class LoginError {

        companion object{

            fun mustFillAllFields(): String{
                return "You can't login without an email and password."
            }

            fun none():String{
                return "None"
            }

        }
    }
    fun isValidForLogin(): String{

        if(login_email.isNullOrEmpty()
            || login_password.isNullOrEmpty()){

            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$login_email, password=$login_password)"
    }
}


data class SendCodeFields(
    var phone:String? = null
){
    class SendCodeError {
        companion object{
            fun mustFillAllFields(): String{
                return "You can't send code without phone."
            }

            fun none():String{
                return "None"
            }
        }
    }

    fun isValidForSendCode(): String{
        if(phone.isNullOrEmpty()){
            return SendCodeError.mustFillAllFields()
        }
        return SendCodeError.none()
    }

    override fun toString(): String {
        return "SendCodeState(phone=$phone)"
    }
}

data class VerifyCodeFields(
    var phone:String? = null,
    var code:String? = null
){
    class VerifyCodeError {
        companion object{
            fun mustFillAllFields(): String{
                return "You can't send code without sms number."
            }

            fun none():String{
                return "None"
            }
        }
    }

    fun isValidForSendCode(): String{
        if(code.isNullOrEmpty()){
            return VerifyCodeError.mustFillAllFields()
        }
        return VerifyCodeError.none()
    }

    override fun toString(): String {
        return "VerifyCodeState(code=$code)"
    }
}


