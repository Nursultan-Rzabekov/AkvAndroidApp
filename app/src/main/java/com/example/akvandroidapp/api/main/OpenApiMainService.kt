package com.example.akvandroidapp.api.main

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.responses.BlogCreateUpdateResponse
import com.example.akvandroidapp.api.main.responses.BlogGetProfileInfoResponse
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
import com.example.akvandroidapp.api.main.responses.ChatHistoryResponse
import com.example.akvandroidapp.ui.main.profile.viewmodel.BlockedDates
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
    @POST("houses/")
    fun createBlog(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("rooms") rooms: RequestBody,
        @Part("floor") floor: RequestBody,
        @Part("address") address: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("city_id") city_id: RequestBody,
        @Part("price") price: RequestBody,
        @Part("beds") beds: RequestBody,
        @Part("guests") guests: RequestBody,
        @Part("rules") rules: RequestBody,
        @Part("near_buildings") near_buildings: RequestBody,
        @Part("house_type_id") house_type_id: RequestBody,
        @Part("blocked_dates") blocked_dates: RequestBody,
        @Part("accommodations") accommodations: RequestBody,
        @Part("discount7days") discount7days: RequestBody,
        @Part("discount30days") discount30days: RequestBody,
        @Part photos: ArrayList<MultipartBody.Part>?
    ): LiveData<GenericApiResponse<BlogCreateUpdateResponse>>

    @GET("houses")
    fun searchListBlogPosts(
//        @Header("Authorization") authorization: String,
        @QueryMap  options : Map<String, String>,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>


    @GET("auth/users/me")
    fun getProfileInfo(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>>

    @PUT("profile")
    fun updateProfileInfo(
        @Header("Authorization") authorization: String,
        @Part("name") first_name: RequestBody,
        @Part userpic: MultipartBody.Part?
    ): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>>


    @GET("my_houses/")
    fun getListMyHouse(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>

    @GET("chats/{id}/messages/")
    fun getChatHistory(
        @Header("Authorization") authorization: String,
        @Path("id") postId:String
    ): LiveData<GenericApiResponse<ChatHistoryResponse>>
}






