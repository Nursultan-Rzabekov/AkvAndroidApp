package com.example.akvandroidapp.ui.main.profile.my_house



sealed class MyHouseStateStateEvent {

    class MyHouseEvent : MyHouseStateStateEvent()

    class MyHouseStateEvent : MyHouseStateStateEvent()

    class None: MyHouseStateStateEvent()
}