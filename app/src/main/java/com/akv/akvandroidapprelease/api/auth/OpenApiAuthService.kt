package com.akv.akvandroidapprelease.api.auth

import androidx.lifecycle.LiveData
import com.akv.akvandroidapprelease.api.auth.network_responses.CodeResponse
import com.akv.akvandroidapprelease.api.auth.network_responses.LoginResponse
import com.akv.akvandroidapprelease.api.auth.network_responses.RegistrationResponse
import com.akv.akvandroidapprelease.api.main.GenericResponse
import com.akv.akvandroidapprelease.util.GenericApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OpenApiAuthService {

    @POST("auth/token/login/")
    @FormUrlEncoded
    fun login(
        @Field("phone") email: String,
        @Field("password") password: String

    ): LiveData<GenericApiResponse<LoginResponse>>

    @POST("auth/users/")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("gender") gender: Int,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("re_password") re_password: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("birth_day") birth_day: String
    ): LiveData<GenericApiResponse<RegistrationResponse>>

    @POST("auth/send_code")
    @FormUrlEncoded
    fun sendCode(
        @Field("phone") phone: String
    ): LiveData<GenericApiResponse<CodeResponse>>


    @POST("auth/users/reset_password/")
    @FormUrlEncoded
    fun forgetEmail(
        @Field("email") email: String
    ): LiveData<GenericApiResponse<GenericResponse>>


    @POST("auth/verify")
    @FormUrlEncoded
    fun verifyCode(
        @Field("phone") phone: String,
        @Field("code") code: String
    ): LiveData<GenericApiResponse<GenericResponse>>
}
