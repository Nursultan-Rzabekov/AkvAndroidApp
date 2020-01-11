package com.example.akvandroidapp.ui.main.messages.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.messages.models.Message
import kotlinx.android.synthetic.main.message_recycler_view_item.view.*

class ChatRecyclerAdapter(
    private val mUserId: String
) : RecyclerView.Adapter<ChatRecyclerAdapter.MessageViewHolder>() {

    private var messageList: MutableList<Message> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_recycler_view_item, parent, false)
        )
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                val message = messageList[position]
                val isMe = message.userId == mUserId
                holder.body.gravity = Gravity.CENTER_VERTICAL

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1.0f
                    gravity = if (isMe) Gravity.END else Gravity.START
                }
                holder.body.layoutParams = params
                holder.body.text = message.message
            }
        }
    }

    fun updateList(data: MutableList<Message>){
        messageList = data
        notifyDataSetChanged()
    }

    fun addMessage(message: Message){
        messageList.add(message)
        notifyItemInserted(messageList.size-1)
    }

    class MessageViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val body = itemView.tvBody
    }
}