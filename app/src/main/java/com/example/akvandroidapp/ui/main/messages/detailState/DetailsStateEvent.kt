package com.example.akvandroidapp.ui.main.messages.detailState


sealed class DetailsStateEvent {

    object ChatDetailEvent: DetailsStateEvent()
    object SendMessageEvent: DetailsStateEvent()
    object None: DetailsStateEvent()
}