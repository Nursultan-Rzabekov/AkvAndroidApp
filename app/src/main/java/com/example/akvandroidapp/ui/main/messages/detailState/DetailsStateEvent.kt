package com.example.akvandroidapp.ui.main.messages.detailState


sealed class DetailsStateEvent {

    class ChatDetailEvent: DetailsStateEvent()
    class None: DetailsStateEvent()
}