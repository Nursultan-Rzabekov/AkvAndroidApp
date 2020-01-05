package com.example.akvandroidapp.ui.main.profile.state

import okhttp3.MultipartBody


sealed class ProfileStateEvent {
    data class CreateNewBlogEvent(
        val _addAdType: String,
        val _addAdGuestsCount: Int,
        val _addAdRoomsCount: Int,
        val _addAdBedsCount: Int,
        val _addAdAddressList: MutableList<String>,
        val _addAdDescription: String,
        val _addAdTitle: String,
        val _addAdPrice: Int,
        val _addAd7DaysDiscount: Int,
        val _addAd30DaysDiscount: Int,
        val image: ArrayList<MultipartBody.Part>? = null
    ): ProfileStateEvent()

    class None: ProfileStateEvent()
}