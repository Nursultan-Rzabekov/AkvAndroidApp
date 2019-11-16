package com.example.akvandroidapp.api.auth

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.auth.network_responses.LoginResponse
import com.example.akvandroidapp.api.auth.network_responses.RegistrationResponse
import com.example.akvandroidapp.util.GenericApiResponse
import retrofit2.http.*

interface OpenApiAuthService {

    @POST("api/auth/token/login/")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LiveData<GenericApiResponse<LoginResponse>>

    @POST("api/auth/users/")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("birth_day") birth_day: String

    ): LiveData<GenericApiResponse<RegistrationResponse>>

}
