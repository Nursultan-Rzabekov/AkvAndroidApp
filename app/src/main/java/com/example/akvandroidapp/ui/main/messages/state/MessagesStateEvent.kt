package com.example.akvandroidapp.ui.main.messages.state


sealed class MessagesStateEvent {
    class ChatInfoEvent: MessagesStateEvent()
    class None: MessagesStateEvent()
}