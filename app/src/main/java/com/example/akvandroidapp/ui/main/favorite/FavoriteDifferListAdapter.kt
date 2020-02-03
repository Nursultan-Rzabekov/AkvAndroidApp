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
import kotlinx.android.synthetic.main.no_favorite_item_layout.view.*
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*

class FavoriteDifferListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null,
    private val interactionCheck: InteractionCheck? = null,
    private val interactionAddMore: InteractionAddMore? = null
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val NO_RESULT = -2
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

    private val NO_RESULTS_BLOG_MARKER = BlogPost(
        NO_RESULT,
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

            NO_RESULT -> {
                Log.e(TAG, "onCreateViewHolder: No results...")
                return NoFavoritesViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.no_favorite_item_layout,
                        parent,
                        false
                    ),
                    requestManager = requestManager,
                    interactionAddMore = interactionAddMore
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
        private val adapter: FavoriteDifferListAdapter
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
            is NoFavoritesViewHolder -> {
                holder.bind()
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

    fun removeAt(position: Int) {
//        val result = differ.currentList.toMutableList()
//        result.removeAt(position)
//        differ.submitList(result)

        differ.removeListListener { previousList, currentList ->
            currentList.removeAt(position)
        }
    }

    fun clearList(){
        differ.submitList(listOf())
        notifyDataSetChanged()
    }

    fun submitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        val currentList = differ.currentList.toMutableList()

        currentList.removeAll { it.id == NO_RESULT || it.id == NO_MORE_RESULTS }

        newList?.forEach {
            currentList.add(it)
        }

        if (currentList.isEmpty())
            currentList.add(
                NO_RESULTS_BLOG_MARKER
            )

        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)

        differ.submitList(currentList)
    }

    fun clearAndSubmitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        if (newList.isNullOrEmpty())
            newList?.add(
                NO_RESULTS_BLOG_MARKER
            )

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
            itemView.search_recycler_item_cost.text = ("${item.price}kzt")
        }
    }

    class NoFavoritesViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        val interactionAddMore: InteractionAddMore?
    ): RecyclerView.ViewHolder(itemView){

        private val goToSearchBtn = itemView.fragment_saved_pages_empty_find_btn

        fun bind(){
            goToSearchBtn.setOnClickListener {
                interactionAddMore?.onAddMoreClicked()
            }
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: BlogPost)
    }

    interface InteractionCheck {
        fun onItemSelected(position: Int, item: BlogPost,boolean: Boolean)
    }

    interface InteractionAddMore{
        fun onAddMoreClicked()
    }
}
