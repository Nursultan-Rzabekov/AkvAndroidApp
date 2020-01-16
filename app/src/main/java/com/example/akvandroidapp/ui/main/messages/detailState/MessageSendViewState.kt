package com.example.akvandroidapp.ui.main.messages.detailState

import com.example.akvandroidapp.entity.UserConversationMessages

data class MessageSendViewState (
    var sendMessageInfo: UserConversationMessages = UserConversationMessages(-1)
)