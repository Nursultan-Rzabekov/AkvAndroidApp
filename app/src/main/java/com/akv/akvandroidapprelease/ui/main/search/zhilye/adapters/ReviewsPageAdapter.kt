package com.akv.akvandroidapprelease.ui.main.search.zhilye.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.entity.Review
import com.akv.akvandroidapprelease.util.DateUtils
import kotlinx.android.synthetic.main.reviews_page_owner_recycler_view_item.view.*
import kotlinx.android.synthetic.main.reviews_page_recycler_view_item.view.*

class ReviewsPageAdapter(
    val userId: Int?,
    val context: Context?,
    val interaction: ReviewPageAdapterInteraction,
    private val requestManager: RequestManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val BLOG_ITEM = 0
    private val MY_BLOG_ITEM = 1

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
            BLOG_ITEM ->{
                return DefaultReviewViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.reviews_page_recycler_view_item,
                        parent,
                        false
                    ),
                    requestManager = requestManager
                )
            }

            MY_BLOG_ITEM -> {
                return OwnerReviewViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.reviews_page_owner_recycler_view_item,
                        parent,
                        false
                    ),
                    context = context,
                    requestManager = requestManager,
                    interaction = interaction
                )
            }

            else -> {
                return DefaultReviewViewHolder(
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
            is DefaultReviewViewHolder -> {
                holder.bind(differ.currentList[position])
            }

            is OwnerReviewViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList[position].id > -1){
            return if (userId == differ.currentList[position].user_id) BLOG_ITEM else MY_BLOG_ITEM
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

    fun submitList(items: List<Review>?){
        val newList = items?.toMutableList()

        val list = differ.currentList.toMutableList()

        newList?.forEach {
            list.add(it)
        }

        differ.submitList(list)
        Log.e("ReviewsPageAdapter", "${differ.currentList}")
    }

    fun clearAndSubmitList(items: List<Review>?){
        differ.submitList(items)
        Log.e("ReviewsPageAdapter", "${differ.currentList}")
    }

    class DefaultReviewViewHolder(
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
    class OwnerReviewViewHolder(
        reviewView: View,
        val context: Context?,
        val requestManager: RequestManager,
        val interaction: ReviewPageAdapterInteraction
    ): RecyclerView.ViewHolder(reviewView){

        private val userpic = reviewView.reviews_page_owner_recycler_view_item_avatar_civ
        private val username = reviewView.reviews_page_owner_recycler_view_item_avatar_nickname
        private val date = reviewView.reviews_page_owner_recycler_view_item_date
        private val rate = reviewView.reviews_page_owner_recycler_view_item_avatar_score
        private val body = reviewView.reviews_page_owner_recycler_view_item_content_tv
        private val deleteBtn = reviewView.reviews_page_owner_recycler_view_item_delete_btn
        private val moreBtn = reviewView.reviews_page_owner_recycler_view_item_more_btn

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

            deleteBtn.setOnClickListener {
                interaction.onDeleteMyReview()
            }

            moreBtn.setOnClickListener {
                val popUpMenu = PopupMenu(context, moreBtn)
                popUpMenu.inflate(R.menu.review_item_menu)
                popUpMenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.review_edit -> {
                            interaction.onEditMyReview(review)
                        }
                    }
                    return@setOnMenuItemClickListener false
                }
                popUpMenu.show()
            }
        }
    }

    interface ReviewPageAdapterInteraction{
        fun onDeleteMyReview()
        fun onEditMyReview(review: Review)
    }

}