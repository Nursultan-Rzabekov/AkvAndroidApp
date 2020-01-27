package com.example.akvandroidapp.ui.main.messages.state

import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import okhttp3.MultipartBody


sealed class MessagesStateEvent {
    class ChatInfoEvent: MessagesStateEvent()
    class None: MessagesStateEvent()
    class OrdersListStateEvent: MessagesStateEvent()
}