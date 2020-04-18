package com.akv.akvandroidapprelease.ui.main.messages.state


import com.akv.akvandroidapprelease.entity.UserChatMessages


class MessagesViewState (
    var myChatFields: MyChatFields = MyChatFields()
)
{
    data class MyChatFields(
        var blogList: List<UserChatMessages> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )
}








