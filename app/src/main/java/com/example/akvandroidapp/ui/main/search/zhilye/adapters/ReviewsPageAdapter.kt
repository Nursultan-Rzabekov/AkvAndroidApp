package com.example.akvandroidapp.ui.main.search.zhilye.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.Review
import com.example.akvandroidapp.ui.main.search.SearchListAdapter
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.reviews_page_recycler_view_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReviewsPageAdapter(
    private val requestManager: RequestManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0

    private val NO_MORE_RESULTS_BLOG_MARKER = Review(
        NO_MORE_RESULTS
    )

    val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Review>(){
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            ReviewRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            NO_MORE_RESULTS ->{
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }
            BLOG_ITEM ->{
                return ReviewViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.reviews_page_recycler_view_item,
                        parent,
                        false
                    ),
                    requestManager = requestManager
                )
            }
            else -> {
                return ReviewViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.reviews_page_recycler_view_item,
                        parent,
                        false
                    ),
                    requestManager = requestManager
                )
            }
        }

    }

    internal inner class ReviewRecyclerChangeCallback(
        private val adapter: ReviewsPageAdapter
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

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ReviewViewHolder -> {
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

    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<Review>
    ){
        for(items in list){
            requestManager
                .load(items.userpic)
                .error(R.drawable.test_image_back)
                .preload()
        }
    }

    fun submitList(items: List<Review>?,isQueryExhausted: Boolean = false){
        val newList = items?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)

        val list = differ.currentList.toMutableList()
        list.removeAll { it.id == NO_MORE_RESULTS }

        newList?.forEach {
            list.add(it)
        }

        differ.submitList(list)
        Log.e("ReviewsPageAdapter", "${differ.currentList}")
    }

    class ReviewViewHolder(
        reviewView: View,
        val requestManager: RequestManager
    ): RecyclerView.ViewHolder(reviewView){

        private val userpic = reviewView.reviews_page_recycler_view_item_avatar_civ
        private val username = reviewView.reviews_page_recycler_view_item_avatar_nickname
        private val date = reviewView.reviews_page_recycler_view_item_date
        private val rate = reviewView.reviews_page_recycler_view_item_avatar_score
        private val body = reviewView.reviews_page_recycler_view_item_content_tv

        fun bind(review: Review){
            requestManager
                .load(review.userpic)
                .error(R.drawable.test_image_back)
                .transition(withCrossFade())
                .into(userpic)

            username.text = review.first_name
            date.text = DateUtils.convertLongToStringDate(review.created_at!!)
            rate.text = review.stars.toString()
            body.text = review.body
        }

    }

}