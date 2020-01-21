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
import com.example.akvandroidapp.ui.main.search.SearchListAdapter
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.my_adds_recycler_view_item.view.*
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*


class MyHouseListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
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
            MyHouseRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    internal inner class MyHouseRecyclerChangeCallback(
        private val adapter: MyHouseListAdapter
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
                    requestManager = requestManager
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyHouseViewHolder -> {
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

    fun submitList(blogList: List<BlogPost>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)
        differ.submitList(newList)
    }

    class MyHouseViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BlogPost) = with(itemView) {
            itemView.my_adds_recycler_view_item_detail_btn.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            if(item.status == 1){
                itemView.my_adds_recycler_view_item_state_btn.text = resources.getString(R.string.deactivate)
                itemView.my_adds_recycler_view_item_state_btn.setTextColor(resources.getColor(R.color.red))
            }
            else{
                itemView.my_adds_recycler_view_item_state_btn.text = resources.getString(R.string.activate)
                itemView.my_adds_recycler_view_item_state_btn.setTextColor(resources.getColor(R.color.green))
            }

            itemView.my_adds_recycler_view_item_state_btn.setOnClickListener {
                interaction?.onStateSelected(adapterPosition,item,my_adds_recycler_view_item_state_btn.text.toString())
                if(my_adds_recycler_view_item_state_btn.text == resources.getString(R.string.activate)){
                    itemView.my_adds_recycler_view_item_state_btn.text = resources.getString(R.string.deactivate)
                    itemView.my_adds_recycler_view_item_state_btn.setTextColor(resources.getColor(R.color.red))
                }
                else{
                    itemView.my_adds_recycler_view_item_state_btn.text = resources.getString(R.string.activate)
                    itemView.my_adds_recycler_view_item_state_btn.setTextColor(resources.getColor(R.color.green))
                }
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
        fun onStateSelected(position: Int,item: BlogPost,text:String)
    }

    override fun onClick(p0: View?) {

    }
}
