package com.example.akvandroidapp.ui.main.messages.models

import android.net.Uri
import com.example.akvandroidapp.util.Constants

abstract class mMessage(
    var _userId: String,
    var _type: Int
)

data class MessageText(
    var userId: String,
    var body: String,
    var type: Int = Constants.MESSAGE_TYPE_TEXT
): mMessage(userId, type)

data class MessagePhoto(
    var userId: String,
    var photo: Uri?,
    var type: Int = Constants.MESSAGE_TYPE_PHOTO
): mMessage(userId, type)

data class MessageDocument(
    var userId: String,
    var fileName: String,
    var fileSize: String,
    var type: Int = Constants.MESSAGE_TYPE_DOC
): mMessage(userId, type)

data class MessageRequest(
    var userId: String,
    var title: String,
    var subtitle: String,
    var photo: Uri?,
    var client: String,
    var type: Int = Constants.MESSAGE_TYPE_REQUEST
): mMessage(userId, type)