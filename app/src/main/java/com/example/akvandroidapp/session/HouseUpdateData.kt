package com.example.akvandroidapp.session

import java.util.*

data class HouseUpdateData(
    var id : Int = -1,
    var title: String? = null,
    var description: String? = null,
    var photosList: MutableList<String> = mutableListOf("https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg"
        , "https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg",
        "https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg",
        "https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg",
        "https://blog.eap.ucop.edu/wp-content/uploads/2016/01/Julie-Huang-27.jpg"),
    var houseRulesList: MutableList<String>? = mutableListOf(),
    var facilitiesList: MutableList<String>? = mutableListOf("Фен","Утюг","Офывфыв","asdd","asdas"),
    var nearByList: MutableList<String>? = mutableListOf(),
    var availableDates: MutableList<Date>? = mutableListOf(),
    var price: Int? = null,
    var address: String? = null
)