package com.example.akvandroidapp.ui.main.messages.detailState

import com.example.akvandroidapp.entity.UserConversationMessages

class DetailsViewState (
    var myChatFields: MyChatDetailsFields = MyChatDetailsFields()
)
{
    data class MyChatDetailsFields(
        var blogList: List<UserConversationMessages> = ArrayList(),
        var page: Int = 1,
        var target :String = "",
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )
}








