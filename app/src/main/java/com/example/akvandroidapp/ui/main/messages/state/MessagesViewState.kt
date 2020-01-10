package com.example.akvandroidapp.ui.main.messages.state

import android.os.Parcelable
import com.example.akvandroidapp.api.main.responses.MessageResponse
import com.example.akvandroidapp.entity.BlogPost
import kotlinx.android.parcel.Parcelize


class MessagesViewState (
    var myChatFields: MyChatFields = MyChatFields()
    )
{
    @Parcelize
    data class MyChatFields(
        var blogList: List<MessageResponse>? = ArrayList(),
        var uri:String = "",
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    ) : Parcelable
}








