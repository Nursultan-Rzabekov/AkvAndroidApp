package com.example.akvandroidapp.api.main

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.responses.*
import com.example.akvandroidapp.util.GenericApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface OpenApiMainService {

    @DELETE("blog/{slug}/delete")
    fun deleteBlogPost(
        @Header("Authorization") authorization: String,
        @Path("slug") slug: String
    ): LiveData<GenericApiResponse<GenericResponse>>

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

    @GET("auth/users/me/")
    fun getProfileInfo(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>>


    @Multipart
    @PATCH("auth/users/me/")
    fun updateProfileInfo(
        @Header("Authorization") authorization: String,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("birth_day") birth_day: RequestBody,
        @Part userpic: MultipartBody.Part?
    ): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>>

    @GET("my_houses/")
    fun getListMyHouse(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<BlogListSearchResponse>>


    @GET("chat_sessions/")
    fun getAllChats(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<AllChatsResponse>>


    @GET("messages/")
    fun getConversations(
        @Header("Authorization") authorization: String,
        @Query("target") target: String,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<ConverstaionsResponse>>

    @Multipart
    @POST("messages/")
    fun sendMessageTo(
        @Header("Authorization") authorization: String,
        @Part("recipient") recipient: RequestBody,
        @Part("body") body: RequestBody,
        @Part images: MultipartBody.Part?
    ):LiveData<GenericApiResponse<UserConversationsInfoResponse>>

    @GET("reservations/")
    fun getReservations(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int
    ): LiveData<GenericApiResponse<HomeListResponse>>

    @GET("houses/{house_id}")
    fun getZhilyeWithHouseId(
        @Path("house_id") house_id: Int
    ): LiveData<GenericApiResponse<ZhilyeResponse>>

    @POST("houses/{house_id}/activate/")
    fun myHouseActivate(
        @Header("Authorization") authorization: String,
        @Path("house_id") house_id: Int
    ): LiveData<GenericApiResponse<MyHouseStateResponse>>

    @POST("houses/{house_id}/deactivate/")
    fun myHouseDeactivate(
        @Header("Authorization") authorization: String,
        @Path("house_id") house_id: Int
    ): LiveData<GenericApiResponse<MyHouseStateResponse>>

    @FormUrlEncoded
    @POST("reservations/")
    fun sendReservationRequest(
        @Header("Authorization") authorization: String,
        @Field("check_in") check_in: String,
        @Field("check_out") check_out: String,
        @Field("guests") guests: Int,
        @Field("house_id") house_id: Int
    ): LiveData<GenericApiResponse<ReservationRequestResponse>>
}






