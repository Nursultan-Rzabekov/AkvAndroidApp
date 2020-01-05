package com.example.akvandroidapp.session

data class AddAdInfo(
    var _addAdType: String = "",
    var _addAdGuestsCount: Int = 0,
    var _addAdRoomsCount: Int = 0,
    var _addAdBedsCount: Int = 0,
    var _addAdAddressList: MutableList<String> = mutableListOf("", "", "", "", ""),
    var _addAdDescription: String = "",
    var _addAdTitle: String = "",
    var _addAdPrice: Int = 0,
    var _addAd7DaysDiscount: Int = 0,
    var _addAd30DaysDiscount: Int = 0
    ) {
}