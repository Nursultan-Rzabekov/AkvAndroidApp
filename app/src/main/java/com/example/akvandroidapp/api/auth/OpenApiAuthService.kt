package com.example.akvandroidapp.api.auth

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.auth.network_responses.CodeResponse
import com.example.akvandroidapp.api.auth.network_responses.LoginResponse
import com.example.akvandroidapp.api.auth.network_responses.RegistrationResponse
import com.example.akvandroidapp.util.GenericApiResponse
import retrofit2.http.*

interface OpenApiAuthService {


    @POST("auth/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String

    ): LiveData<GenericApiResponse<LoginResponse>>

    @POST("auth/register")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("gender") gender: Int,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("birth_day") birth_day: String

    ): LiveData<GenericApiResponse<RegistrationResponse>>

    @POST("auth/send_code")
    @FormUrlEncoded
    fun sendCode(
        @Field("phone") phone: String

    ): LiveData<GenericApiResponse<CodeResponse>>


    @POST("auth/verify")
    @FormUrlEncoded
    fun verifyCode(
        @Field("phone") phone: String,
        @Field("code") code: String

    ): LiveData<GenericApiResponse<CodeResponse>>


}
