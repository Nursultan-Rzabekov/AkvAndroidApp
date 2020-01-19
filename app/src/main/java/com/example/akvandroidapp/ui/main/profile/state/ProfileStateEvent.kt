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
        val facilitiesList: List<String>,
        val nearbyList: List<String>,
        val rulesList: List<String>,
        val image: ArrayList<MultipartBody.Part>? = null
    ): ProfileStateEvent()

    class GetProfileInfoEvent(): ProfileStateEvent()

    data class EditProfileInfoEvent(
        var phone: String? = null,
        var birth_day: String? = null,
        var gender: Int? = null,
//        var first_name: String? = null,
//        var last_name: String? = null,
        var email: String? = null,
        val image: MultipartBody.Part? = null
    ): ProfileStateEvent()

    class MyHouseEvent : ProfileStateEvent()

    class None: ProfileStateEvent()
}