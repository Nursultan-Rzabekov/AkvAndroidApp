package com.example.akvandroidapp.ui.main.messages.detailState

import android.os.Parcelable
import com.example.akvandroidapp.entity.UserConversationMessages
import kotlinx.android.parcel.Parcelize

class DetailsViewState (
    var myChatFields: MyChatDetailsFields = MyChatDetailsFields(),

    var sendMessageFields: SendMessageFields = SendMessageFields()
)
{
    data class MyChatDetailsFields(
        var blogList: List<UserConversationMessages> = ArrayList(),
        var page: Int = 1,
        var target :String = "",
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )


    @Parcelize
    data class SendMessageFields(
        var blogPost: UserConversationMessages? = null,
        var messageBody: String = "",
        var email: String = ""
    ) : Parcelable
}








