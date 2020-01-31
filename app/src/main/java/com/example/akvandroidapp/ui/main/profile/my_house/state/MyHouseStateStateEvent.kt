package com.example.akvandroidapp.ui.main.profile.my_house.state



sealed class MyHouseStateStateEvent {

    class MyHouseEvent : MyHouseStateStateEvent()

    class MyHouseStateEvent : MyHouseStateStateEvent()

    class MyHouseZhilyeEvent: MyHouseStateStateEvent()

    class None: MyHouseStateStateEvent()
}