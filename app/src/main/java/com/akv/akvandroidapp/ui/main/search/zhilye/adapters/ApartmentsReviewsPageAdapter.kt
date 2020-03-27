package com.akv.akvandroidapp.ui.main.search.zhilye.adapters

import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.entity.Review
import com.akv.akvandroidapp.util.DateUtils
import com.akv.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.reviews_band_recycler_view_item.view.*
import kotlinx.android.synthetic.main.show_more_reviews_item.view.*

class ApartmentsReviewsPageAdapter(
    private val requestManager: RequestManager,
    val showMoreReviewInteraction: ShowMoreReviewInteraction
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0
    private val SHOW_MORE_ITEMS = -2

    private val NO_MORE_RESULTS_BLOG_MARKER = Review(
        NO_MORE_RESULTS
    )
    private val SHOW_MORE_ITEMS_MARKER = Review(
        SHOW_MORE_ITEMS
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
                        R.layout.no_reviews_item,
                        parent,
                        false
                    )
                )
            }
            BLOG_ITEM ->{
                return ReviewViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.reviews_band_recycler_view_item,
                        parent,
                        false
                    ),
                    requestManager = requestManager
                )
            }
            SHOW_MORE_ITEMS -> {
                return ShowMoreReviewViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.show_more_reviews_item,
                        parent,
                        false
                    ),
                    showMoreReviewInteraction = showMoreReviewInteraction
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
        private val adapter: ApartmentsReviewsPageAdapter
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
            is ShowMoreReviewViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList[position].id > -1){
            return BLOG_ITEM
        }
        else if (differ.currentList[position].id == SHOW_MORE_ITEMS)
            return SHOW_MORE_ITEMS
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

    fun submitList(items: List<Review>?){
        val newList = items?.toMutableList()?.take(2)?.toMutableList()
        if (newList?.size!! > 0){
            newList.add(SHOW_MORE_ITEMS_MARKER)
        }
        if (newList.isEmpty()) {
            newList.add(NO_MORE_RESULTS_BLOG_MARKER)
            newList.add(SHOW_MORE_ITEMS_MARKER)
        }
        differ.submitList(newList)
        Log.e("ReviewsPageAdapter", "${differ.currentList}")
    }

    class ReviewViewHolder(
        reviewView: View,
        val requestManager: RequestManager
    ): RecyclerView.ViewHolder(reviewView){

        private val userpic = reviewView.reviews_band_civ
        private val username = reviewView.reviews_band_username_tv
        private val date = reviewView.reviews_band_date_tv
        private val rate = reviewView.reviews_band_rating_tv
        private val body = reviewView.reviews_band_comment_tv

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

    class ShowMoreReviewViewHolder(
        moreView: View,
        private val showMoreReviewInteraction: ShowMoreReviewInteraction
    ): RecyclerView.ViewHolder(moreView){

        private val tv = moreView.show_more_reviews_tv

        fun bind(){
            tv.setText(tv.text.toString(), TextView.BufferType.SPANNABLE)
            val span4 = tv.text as Spannable
            span4.setSpan(UnderlineSpan(), 0, tv.text.toString().length, 0)

            tv.setOnClickListener {
                showMoreReviewInteraction.onShowMorePressed()
            }
        }

    }

    interface ShowMoreReviewInteraction{
        fun onShowMorePressed()
    }
}