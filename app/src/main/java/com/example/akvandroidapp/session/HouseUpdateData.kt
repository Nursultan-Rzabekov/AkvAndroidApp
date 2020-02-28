package com.example.akvandroidapp.session

import com.example.akvandroidapp.ui.main.profile.my_house.adapters.GalleryPhoto
import java.util.*

data class HouseUpdateData(
    var id : Int = -1,
    var title: String? = null,
    var description: String? = null,
    var photosList: MutableList<GalleryPhoto> = mutableListOf(
//        GalleryPhoto("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg", null),
//        GalleryPhoto("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg", null),
//        GalleryPhoto("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg", null),
//        GalleryPhoto("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg", null),
//        GalleryPhoto("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg", null)
    ),
    var houseRulesList: MutableList<String>? = mutableListOf(),
    var facilitiesList: MutableList<String>? = mutableListOf(),
    var nearByList: MutableList<String>? = mutableListOf(),
    var blockedDates: MutableList<Date>? = mutableListOf(),
    var price: Int? = null,
    var address: String? = null
)