package com.example.akvandroidapp.ui.main.messages.detailState

sealed class MessageSendStateEvent {
    class MessageSendEvent: MessageSendStateEvent()
    class SendNone: MessageSendStateEvent()
}