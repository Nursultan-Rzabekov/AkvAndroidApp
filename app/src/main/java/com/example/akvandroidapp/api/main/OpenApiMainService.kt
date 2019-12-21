package com.example.akvandroidapp.api.main

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.responses.BlogCreateUpdateResponse
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
import com.example.akvandroidapp.util.GenericApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface OpenApiMainService {


//    @GET("account/properties")
//    fun getAccountProperties(
//        @Header("Authorization") authorization: String
//    ): LiveData<GenericApiResponse<AccountProperties>>

    @PUT("account/properties/update")
    @FormUrlEncoded
    fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): LiveData<GenericApiResponse<GenericResponse>>

    @PUT("account/change_password/")
    @FormUrlEncoded
    fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): LiveData<GenericApiResponse<GenericResponse>>




    @GET("blog/{slug}/is_author")
    fun isAuthorOfBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): LiveData<GenericApiResponse<GenericResponse>>


    @DELETE("blog/{slug}/delete")
    fun deleteBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): LiveData<GenericApiResponse<GenericResponse>>

    @Multipart
    @PUT("blog/{slug}/update")
    fun updateBlog(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): LiveData<GenericApiResponse<BlogCreateUpdateResponse>>


    @Multipart
    @POST("blog/create")
    fun createBlog(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("body") body: RequestBody,
        @Part image: MultipartBody.Part?
    ): LiveData<GenericApiResponse<BlogCreateUpdateResponse>>



    @GET("houses")
    fun searchListBlogPosts(
//        @Header("Authorization") authorization: String,
        @Query("city__name") city__name: String,
        @Query("price__gte") price__gte: Int,
        @Query("price__lte") price__lte: Int,
        @Query("room__gte") room__gte: Int,
        @Query("room__lte") room__lte: Int,
        @Query("beds_gte") beds_gte: Int,
        @Query("beds_lte") beds_lte: Int,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>
}









