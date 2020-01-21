package com.example.akvandroidapp.ui.main.messages.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.messages.models.*
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.message_doc_recycler_view_item.view.*
import kotlinx.android.synthetic.main.message_photo_recycler_view_item.view.*
import kotlinx.android.synthetic.main.message_recycler_view_item.view.*

class ChatRecyclerAdapter(
    private val requestManager: RequestManager,
    private val mUserId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList: MutableList<mMessage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            Constants.MESSAGE_TYPE_TEXT -> {
                return MessageTextViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.message_recycler_view_item, parent, false)
                )
            }
            Constants.MESSAGE_TYPE_PHOTO -> {
                return MessagePhotoViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.message_photo_recycler_view_item, parent, false),
                    requestManager = requestManager
                )
            }
            Constants.MESSAGE_TYPE_DOC -> {
                return MessageDocViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.message_doc_recycler_view_item, parent, false)
                )
            }
            else -> {
                return MessageTextViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.message_recycler_view_item, parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position]._type
    }

    override fun getItemCount(): Int = messageList.size

    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<mMessage>
    ){
        for(message in list){
            if (message._type == Constants.MESSAGE_TYPE_PHOTO)
                if ((message as MessagePhoto).photo != null)
                requestManager
                    .load(message.photo)
                    .error(R.drawable.test_image_back)
                    .preload()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]
        val isMe = message._userId == mUserId

        when (holder) {
            is MessageTextViewHolder -> {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1.0f
                    gravity = if (isMe) Gravity.END else Gravity.START
                }

                holder.layout.layoutParams = params
                holder.bind(message as MessageText)
            }
            is MessagePhotoViewHolder -> {
                val params = LinearLayout.LayoutParams(
                    holder.photo.layoutParams.width,
                    holder.photo.layoutParams.height
                ).apply {
                    weight = 1.0f
                    gravity = if (isMe) Gravity.END else Gravity.START
                }

                holder.layout.layoutParams = params
                holder.bind(message as MessagePhoto)
            }
            is MessageDocViewHolder -> {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1.0f
                    gravity = if (isMe) Gravity.END else Gravity.START
                }
                holder.layout.layoutParams = params
                holder.bind(message as MessageDocument)
            }
        }
    }

    fun updateList(data: MutableList<mMessage>){
        messageList = data
        notifyDataSetChanged()
    }

    fun addMessage(message: mMessage){
        messageList.add(message)
        notifyItemInserted(messageList.size-1)
    }

    fun clearMessages(){
        messageList.clear()
        notifyDataSetChanged()
    }

    class MessageTextViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val body = itemView.tvBody
        val layout = itemView.message_recycler_view_item_layout

        fun bind(message: MessageText){
            body.text = message.body
        }
    }

    class MessagePhotoViewHolder(
        itemView: View,
        val requestManager: RequestManager
    ): RecyclerView.ViewHolder(itemView){
        val photo = itemView.message_photo_recycler_view_item_iv
        val layout = itemView.message_photo_recycler_view_item_layout

        fun bind(message: MessagePhoto){

            if(message.photo != null){
                Glide.with(photo.context)
                    .load(message.photo)
                    .error(R.drawable.test_image_back)
                    .transition(withCrossFade())
                    .fitCenter()
                    .into(photo)
            }

            if(message.image != null){
                Glide.with(photo.context)
                    .load("http://akv-technopark.herokuapp.com${message.image}")
                    .error(R.drawable.test_image_back)
                    .transition(withCrossFade())
                    .fitCenter()
                    .into(photo)
            }
        }
    }

    class MessageDocViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        val file_name = itemView.message_doc_recycler_view_item_name
        val file_size = itemView.message_doc_recycler_view_item_size
        val layout = itemView.message_doc_recycler_view_item_name_layout_card

        fun bind(message: MessageDocument){
            file_name.text = message.fileName
            file_size.text = message.fileSize
        }
    }
}