package com.example.akvandroidapp.ui.main.profile.my_house.state



sealed class MyHouseStateStateEvent {

    class MyHouseEvent : MyHouseStateStateEvent()

    class MyHouseStateEvent : MyHouseStateStateEvent()

    class MyHouseZhilyeEvent: MyHouseStateStateEvent()

    data class MyHouseUpdateEvent(
        var house_id: Int,
        var title: String?,
        var description: String?,
        var address: String?,
        var price: Int?,
        var photoList: List<String>?,
        var facilitiesList: List<String>?,
        var rulesList: List<String>?,
        var nearsList: List<String>?,
        var datesList: List<String>?
    ): MyHouseStateStateEvent()

    class None: MyHouseStateStateEvent()
}