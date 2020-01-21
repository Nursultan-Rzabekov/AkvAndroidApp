package com.example.akvandroidapp.ui.main.messages.detailState

import com.example.akvandroidapp.entity.UserConversationImages
import com.example.akvandroidapp.entity.UserConversationMessages
import okhttp3.MultipartBody

class DetailsViewState (
    var myChatFields: MyChatDetailsFields = MyChatDetailsFields(),

    var sendMessageFields: SendMessageFields = SendMessageFields()
)
{
    data class MyChatDetailsFields(
        var blogList: List<UserConversationMessages> = ArrayList(),
        var blogListImages: List<UserConversationImages?> = ArrayList(),
        var page: Int = 1,
        var target :String = "",
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )


    data class SendMessageFields(
        var blogPost: UserConversationMessages? = null,
        var messageBody: String = "",
        var email: String = "",
        var images: MultipartBody.Part? = null
    )
}








