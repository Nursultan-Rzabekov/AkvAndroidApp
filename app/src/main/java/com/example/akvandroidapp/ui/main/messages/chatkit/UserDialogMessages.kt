package com.example.akvandroidapp.ui.main.messages.chatkit

import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser


data class UserDialogMessages(
    private var id: String,
    var first_name: String? = null,
    var userpic: String? = null,
    var users: ArrayList<User>,
    var message: Message,
    private var unreadCount: Int = 0

    ) : IDialog<Message>
{
    override fun getDialogPhoto(): String {
        return  userpic.toString()
    }

    override fun setLastMessage(message: Message) {
        this.message = message
    }

    override fun getUsers(): MutableList<out IUser> {
        return users.toMutableList()
    }

    override fun getLastMessage(): Message {
        return message
    }

    override fun getDialogName(): String {
        return first_name.toString()
    }

    override fun getUnreadCount(): Int {
        return unreadCount
    }

    override fun getId(): String {
        return id
    }

}
