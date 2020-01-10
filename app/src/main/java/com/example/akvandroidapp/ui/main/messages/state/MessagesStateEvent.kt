package com.example.akvandroidapp.ui.main.messages.state

import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import okhttp3.MultipartBody


sealed class MessagesStateEvent {
    data class ChatInfoEvent(
        var uri: String? = null
    ): MessagesStateEvent()

    class None: MessagesStateEvent()
}