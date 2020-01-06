package com.example.akvandroidapp.api.main

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.responses.BlogCreateUpdateResponse
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
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
    @POST("houses")
    fun createBlog(
        @Header("accept") type: String,
        @Header("Authorization") authorization: String,
        @Part("name") name: String,
        @Part("description") description: String,
        @Part("rooms") rooms: Int,
        @Part("floor") floor: Int,
        @Part("address") address: String,
        @Part("longitude") longitude: Double,
        @Part("latitude") latitude: Double,
        @Part("city_id") city_id: Int,
        @Part("price") price: Int,
        @Part("beds") beds: Int,
        @Part("guests") guests: Int,
        @Part("rules") rules: String,
        @Part("near_buildings") near_buildings: String,
        @Part("house_type_id") house_type_id: Int,
        @Part("blocked_dates") blocked_dates: List<BlockedDates>,
        @Part("accommodations") accommodations: String,
        @Part photos: ArrayList<MultipartBody.Part>?
    ): LiveData<GenericApiResponse<BlogCreateUpdateResponse>>

    @GET("houses")
    fun searchListBlogPosts(
//        @Header("Authorization") authorization: String,
        @QueryMap  options : Map<String, String>,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>
}









