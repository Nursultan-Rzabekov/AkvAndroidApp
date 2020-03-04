package com.example.akvandroidapp.ui.main.messages.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.UserChatMessages
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.chats_recycler_view_item.view.*

class ChatListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0

    private val NO_MORE_RESULTS_BLOG_MARKER = UserChatMessages(
        NO_MORE_RESULTS,
        "" ,
        "",
        "",
        ""

    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserChatMessages>() {

        override fun areItemsTheSame(oldItem: UserChatMessages, newItem: UserChatMessages): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserChatMessages, newItem: UserChatMessages): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            BlogRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType){
            NO_MORE_RESULTS ->{
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            BLOG_ITEM ->{
                return BlogViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.chats_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return BlogViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.chats_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: ChatListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BlogViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList[position].id > -1){
            return BLOG_ITEM
        }
        return differ.currentList[position].id
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Prepare the images that will be displayed in the RecyclerView.
    // This also ensures if the network connection is lost, they will be in the cache
    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<UserChatMessages>
    ){
        for(allChats in list){
            requestManager
                .load(allChats.userpic)
                .error(R.drawable.test_image_back)
                .preload()
        }
    }


    fun submitList(blogList: List<UserChatMessages>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)
        differ.submitList(newList)
    }

    class BlogViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserChatMessages) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            requestManager
                .load(item.userpic)
                .error(R.drawable.test_image_back)
                .transition(withCrossFade())
                .into(itemView.chats_recycler_view_item_civ)

            itemView.chats_recycler_view_item_from_tv.text = item.email
            itemView.chats_recycler_view_item_last_message_tv.text = item.first_name

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: UserChatMessages)
    }


}
