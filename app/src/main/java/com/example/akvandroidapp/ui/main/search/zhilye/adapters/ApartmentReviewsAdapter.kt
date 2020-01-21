package com.example.akvandroidapp.ui.main.search.zhilye.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.util.GenericViewHolder

class ApartmentReviewsAdapter(
    private val requestManager: RequestManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private var reviews : MutableList<BlogPost> = ArrayList()
    private val NO_REVIEWS = -1

    private val NO_REVIEWS_MARKER = BlogPost(
        NO_REVIEWS,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            NO_REVIEWS -> {
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.no_reviews_item,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return ReviewsViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.reviews_band_recycler_view_item,
                        parent,
                        false
                    ),
                    requestManager = requestManager
                )
            }
        }
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ReviewsViewHolder -> {
                holder.bind(reviews[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (reviews[position].id > 0)
            return 0
        return NO_REVIEWS
    }

    fun submitList(list: List<BlogPost>){
        if (list.isNotEmpty())
            reviews = list.toMutableList()
        else reviews = mutableListOf(NO_REVIEWS_MARKER)
        notifyDataSetChanged()
    }

    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<BlogPost>
    ){
        for(blogPost in list){

            if(blogPost.image != null){
                requestManager
                    .load(blogPost.image)
                    .error(R.drawable.test_image_back)
                    .preload()
            }
            else{
                requestManager
                    .load(R.drawable.fragment_appartments_image_default)
                    .preload()
            }

        }
    }

    class ReviewsViewHolder(
        itemView: View,
        requestManager: RequestManager
    ): RecyclerView.ViewHolder(itemView) {
        fun bind(item: BlogPost){

        }
    }
}