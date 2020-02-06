package com.example.akvandroidapp.ui.main.profile.add_ad


import okhttp3.MultipartBody
import java.util.*
import kotlin.collections.ArrayList


sealed class AddAdStateEvent {
    data class CreateNewBlogEvent(
        val _addAdType: String,
        val _addAdGuestsCount: Int,
        val _addAdRoomsCount: Int,
        val _addAdBedsCount: Int,
        val _addAdAddress: String,
        var _addAdAddressRegionId: Int,
        var _addAdAddressCityId: Int,
        var _addAdAddressCountry: Int,
        val _addAdDescription: String,
        val _addAdTitle: String,
        val _addAdPrice: Int,
        val _addAd7DaysDiscount: Int,
        val _addAd30DaysDiscount: Int,
        val facilitiesList: List<String>,
        val nearbyList: List<String>,
        val rulesList: List<String>,
        val _availableList: List<Date>,
        val image: ArrayList<MultipartBody.Part>? = null
    ): AddAdStateEvent()

    class None: AddAdStateEvent()
}