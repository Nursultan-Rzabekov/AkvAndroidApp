package com.example.akvandroidapp.api.main.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChatHistoryResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("uri")
    @Expose
    var uri: String,

    @SerializedName("messages")
    @Expose
    var messages: List<MessageResponse>?

) {

}