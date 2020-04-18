package com.akv.akvandroidapprelease.ui.main.messages.detailState

import com.akv.akvandroidapprelease.entity.UserConversationMessages
import com.akv.akvandroidapprelease.entity.UserConversationSendMessages
import okhttp3.MultipartBody

class DetailsViewState (
    var myChatFields: MyChatDetailsFields = MyChatDetailsFields(),

    var sendMessageFields: SendMessageFields = SendMessageFields()
)
{
    data class MyChatDetailsFields(
        var blogList: List<UserConversationMessages> = ArrayList(),
//        var blogListImages: List<UserConversationImages?> = ArrayList(),
        var page: Int = 1,
        var count: Int = 0,
        var target :Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )


    data class SendMessageFields(
        var blogPost: UserConversationSendMessages? = null,
        var sended:Boolean = false,
        var messageBody: String = "1",
        var userId: Int = 1,
        var images: MultipartBody.Part? = null
    )
}








