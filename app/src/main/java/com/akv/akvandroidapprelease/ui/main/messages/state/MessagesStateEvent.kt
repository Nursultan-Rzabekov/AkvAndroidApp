package com.akv.akvandroidapprelease.ui.main.messages.state


sealed class MessagesStateEvent {
    object ChatInfoEvent: MessagesStateEvent()
    object None: MessagesStateEvent()
}