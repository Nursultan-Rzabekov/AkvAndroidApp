package com.akv.akvandroidapp.ui.main.messages.models

import android.net.Uri
import com.akv.akvandroidapp.ui.main.messages.chatkit.User
import com.akv.akvandroidapp.util.Constants
import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.*

abstract class mMessage(
    var _userId: Int,
    var _type: Int
) : IMessage

data class MessageText(
    var userId: Int,
    var body: String,
    var created_at:Date,
    var user: User = User("0","Nursultan","http://i.imgur.com/pv1tBmT.png",true),
    var type: Int = Constants.MESSAGE_TYPE_TEXT
): mMessage(userId, type) {
    override fun getId(): String {
        return userId.toString()
    }

    override fun getCreatedAt(): Date {
        return created_at
    }

    override fun getUser(): IUser {
        return user
    }

    override fun getText(): String {
        return body
    }
}


data class MessagePhoto(
    var userId: Int,
    var photo: Uri? = null,
    var image: String? = null,
    var created_at:Date,
    var user: User = User("1","Nursultan","http://i.imgur.com/R3Jm1CL.png",true),
    var type: Int = Constants.MESSAGE_TYPE_PHOTO
): mMessage(userId, type), MessageContentType.Image{
    override fun getId(): String {
        return userId.toString()
    }

    override fun getCreatedAt(): Date {
        return created_at
    }

    override fun getUser(): IUser {
        return user
    }

    override fun getText(): String {
        return image.toString()
    }

    override fun getImageUrl(): String? {
        return image
    }
}

data class MessageDocument(
    var userId: Int,
    var fileName: String,
    var fileSize: String,
    var created_at:Date,
    var user: User = User("1","Nursultan","http://i.imgur.com/R3Jm1CL.png",true),
    var type: Int = Constants.MESSAGE_TYPE_DOC
): mMessage(userId, type){
    override fun getId(): String {
        return userId.toString()
    }

    override fun getCreatedAt(): Date {
        return created_at
    }

    override fun getUser(): IUser {
        return user
    }

    override fun getText(): String {
        return fileName
    }
}

data class MessageRequest(
    var userId: Int,
    var title: String,
    var subtitle: String,
    var photo: Uri?,
    var client: String,
    var created_at:Date,
    var user: User = User("1","Nursultan","http://i.imgur.com/R3Jm1CL.png",true),
    var type: Int = Constants.MESSAGE_TYPE_REQUEST
): mMessage(userId, type){
    override fun getId(): String {
        return userId.toString()
    }

    override fun getCreatedAt(): Date {
        return created_at
    }

    override fun getUser(): IUser {
        return user
    }

    override fun getText(): String {
        return title
    }
}