package com.example.akvandroidapp.ui.main.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.util.Converters
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*

class SearchListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null,
    private val interactionCheck: InteractionCheck? = null
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        false,
        "",
        0.0
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BlogPost>() {

        override fun areItemsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BlogPost, newItem: BlogPost): Boolean {
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

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: SearchListAdapter
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
        list: List<BlogPost>
    ){
        for(blogPost in list){
            requestManager
                .load(blogPost.image)
                .error(R.drawable.test_image_back)
                .preload()
        }
    }

    fun clearList(){
        differ.submitList(listOf())
        notifyDataSetChanged()
    }

    fun submitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)

        val list = differ.currentList.toMutableList()
        list.removeAll { it.id == NO_MORE_RESULTS }

        newList?.forEach {
            list.add(it)
        }
        differ.submitList(list)
    }

    fun clearAndSubmitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)

        differ.submitList(newList)
    }

    class BlogViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?,
        private val interactionCheck: InteractionCheck?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlogPost) = with(itemView) {

            Log.e("SearchListAdapter", "is favorite ${item.is_favourite}")
            itemView.search_recycler_item_favourite_btn.isChecked = item.is_favourite

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.search_recycler_item_favourite_btn.setOnClickListener {
                val isChecked = itemView.search_recycler_item_favourite_btn.isChecked
                interactionCheck?.onItemSelected(adapterPosition, item, isChecked)
            }

            requestManager
                .load(item.image)
                .error(R.drawable.test_image_back)
                .transition(withCrossFade())
                .into(itemView.search_recycler_item_image_back)

            itemView.search_recycler_item_header.text = item.name
            itemView.search_recycler_item_location.text = item.city.toString()
            itemView.search_recycler_item_cost.text = ("${Converters.pretifyPrice(item.price)}kzt")
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BlogPost)
    }

    interface InteractionCheck {
        fun onItemSelected(position: Int, item: BlogPost,boolean: Boolean)
    }

}
