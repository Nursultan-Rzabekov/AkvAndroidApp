package com.example.akvandroidapp.ui.main.profile.my_house

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.AddAdInfo
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.my_adds_recycler_view_item.view.*
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*


class MyHouseListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null,
    private val interactionCheck: InteractionCheck? = null
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0
    private val NO_MORE_RESULTS_BLOG_MARKER = BlogPost(
        NO_MORE_RESULTS,
        "" ,
        0,
        0,
        false,
        0.0,
        0.0,
        "",
        "",
        0,
        0,
        "",
        0.0
    )

    private var items: MutableList<BlogPost> = ArrayList()

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
                return MyHouseViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.my_adds_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    interactionCheck = interactionCheck,
                    requestManager = requestManager
                )
            }
            else -> {
                return MyHouseViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.my_adds_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    interactionCheck = interactionCheck,
                    requestManager = requestManager
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyHouseViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun submitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()

        newList?.let {
            items = newList
            Log.d("sea","Search : + ${items}")
        }
    }

    class MyHouseViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?,
        private val interactionCheck: InteractionCheck?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlogPost) = with(itemView) {
            itemView.my_adds_recycler_view_item_detail_btn.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            requestManager
                .load(item.image)
                .error(R.drawable.test_image_back)
                .transition(withCrossFade())
                .into(itemView.my_adds_recycler_view_item_iv)
            itemView.my_adds_recycler_view_item_title.text = item.name
            itemView.my_adds_recycler_view_item_price.text = item.price.toString()
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BlogPost)
    }

    interface InteractionCheck {
        fun onItemSelected(position: Int, item: BlogPost,boolean: Boolean)
    }

    override fun onClick(p0: View?) {

    }
}
