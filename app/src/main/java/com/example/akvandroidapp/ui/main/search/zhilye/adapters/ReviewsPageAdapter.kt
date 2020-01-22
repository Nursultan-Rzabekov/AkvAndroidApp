package com.example.akvandroidapp.ui.main.search.zhilye.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.Review
import kotlinx.android.synthetic.main.reviews_page_recycler_view_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReviewsPageAdapter(
    private val requestManager: RequestManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val formatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val formatString = SimpleDateFormat("LLLL yyyy", Locale.getDefault())

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
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reviews_page_recycler_view_item,
                parent,
                false
            ),
            requestManager = requestManager,
            formatDate = formatDate,
            formatString = formatString
        )
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

    fun submitList(items: MutableList<Review>){
        val newList = items.toMutableList()
        differ.submitList(newList)
        Log.e("ReviewsPageAdapter", "${differ.currentList}")
    }

    class ReviewViewHolder(
        reviewView: View,
        val requestManager: RequestManager,
        val formatDate: SimpleDateFormat,
        val formatString: SimpleDateFormat
    ): RecyclerView.ViewHolder(reviewView){

        private val userpic = reviewView.reviews_page_recycler_view_item_avatar_civ
        private val username = reviewView.reviews_page_recycler_view_item_avatar_nickname
        private val date = reviewView.reviews_page_recycler_view_item_date
        private val rate = reviewView.reviews_page_recycler_view_item_avatar_score
        private val body = reviewView.reviews_page_recycler_view_item_content_tv

        fun bind(review: Review){
            if (review.userpic != null){
                requestManager
                    .load(review.userpic)
                    .error(R.drawable.test_image_back)
                    .transition(withCrossFade())
                    .into(userpic)
            }
            username.text = review.first_name
            val time = formatDate.parse(review.created_at.toString())
            date.text = formatString.format(time!!).toString()
            rate.text = review.stars.toString()
            body.text = review.body
        }

    }

}