package com.example.akvandroidapp.ui.main.favorite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*


class FavoriteListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null,
    private val interactionCheck: InteractionCheck? = null
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0

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
                return BlogViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.search_result_recycler_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    interactionCheck = interactionCheck,
                    requestManager = requestManager
                )
            }
            else -> {
                return BlogViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.search_result_recycler_item,
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
            is BlogViewHolder -> {
                holder.bind(items[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Prepare the images that will be displayed in the RecyclerView.
    // This also ensures if the network connection is lost, they will be in the cache
    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<BlogPost>
    ){
        for(blogPost in list){
            requestManager
                .load(blogPost.image)
                .error(R.drawable.test_image_back)
                .preload()
        }
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun submitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        newList?.let {
            items = newList
        }
    }

    class BlogViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?,
        private val interactionCheck: InteractionCheck?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlogPost) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.search_recycler_item_favourite_btn.isChecked = true

            itemView.search_recycler_item_favourite_btn.setOnClickListener {
                if(itemView.search_recycler_item_favourite_btn.isChecked){
                    interactionCheck?.onItemSelected(adapterPosition,item,true)
                }
                else{
                    interactionCheck?.onItemSelected(adapterPosition,item,false)
                }
            }

            requestManager
                .load(item.image)
                .error(R.drawable.test_image_back)
                .transition(withCrossFade())
                .into(itemView.search_recycler_item_image_back)
            itemView.search_recycler_item_header.text = item.name
            itemView.search_recycler_item_location.text = item.city.toString()
            itemView.search_recycler_item_cost.text = item.price.toString()
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
