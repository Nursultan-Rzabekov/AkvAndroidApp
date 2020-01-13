package com.example.akvandroidapp.ui.main.messages.models

import android.net.Uri
import com.example.akvandroidapp.util.Constants

data class Message (
    var userId: String,
    var message: String,
    var type: Int = Constants.MESSAGE_TYPE_TEXT,
    var photo: Uri? = null,
    var fileName: String = "",
    var fileSize: String = ""
)