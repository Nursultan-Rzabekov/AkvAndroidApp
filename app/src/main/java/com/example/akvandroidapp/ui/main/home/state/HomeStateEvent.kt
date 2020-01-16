package com.example.akvandroidapp.ui.main.home.state


sealed class HomeStateEvent {
    class HomeInfoEvent: HomeStateEvent()
    class None: HomeStateEvent()
}