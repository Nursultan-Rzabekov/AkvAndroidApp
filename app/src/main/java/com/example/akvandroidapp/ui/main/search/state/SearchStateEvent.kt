package com.example.akvandroidapp.ui.main.search.state

import okhttp3.MultipartBody


sealed class SearchStateEvent {

    class BlogSearchEvent : SearchStateEvent()

    class CheckAuthorOfBlogPost: SearchStateEvent()

    class DeleteBlogPostEvent: SearchStateEvent()

    class DeleteFavoriteItemEvent(
        val houseId:Int
    ): SearchStateEvent()

    class СreateFavoriteItemEvent(
        val houseId:Int
    ): SearchStateEvent()

    data class UpdateBlogPostEvent(
        val title: String,
        val body: String,
        val image: MultipartBody.Part?
    ): SearchStateEvent()

    class None: SearchStateEvent()
}