package com.akv.akvandroidapprelease.ui.main.search.zhilye.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.entity.BlogPost
import com.akv.akvandroidapprelease.util.GenericViewHolder
import kotlinx.android.synthetic.main.recommendations_recycler_view_item.view.*

class RecommendationsAdapter(
    private val requestManager: RequestManager
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private var recommendations: MutableList<BlogPost> = ArrayList()
    private val NO_RECOMMENDATIONS = -1

    private val NO_RECOMMENDATIONS_MARKER = BlogPost(
        NO_RECOMMENDATIONS,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            NO_RECOMMENDATIONS -> {
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.no_recommendations,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return RecommendationViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.recommendations_recycler_view_item,
                        parent,
                        false
                    ),
                    requestManager = requestManager
                )
            }
        }
    }

    override fun getItemCount(): Int = recommendations.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RecommendationViewHolder -> {
                holder.bind(recommendations[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (recommendations[position].id > 0)
            return 0
        return NO_RECOMMENDATIONS
    }

    fun submitList(list: List<BlogPost>){
        if (list.isNotEmpty())
            recommendations = list.toMutableList()
        else recommendations = mutableListOf(NO_RECOMMENDATIONS_MARKER)
        notifyDataSetChanged()
    }

    // Prepare the images that will be displayed in the RecyclerView.
    // This also ensures if the network connection is lost, they will be in the cache
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

    class RecommendationViewHolder(
        itemView: View,
        val requestManager: RequestManager
    ): RecyclerView.ViewHolder(itemView){

        fun bind(item: BlogPost) = with(itemView){
            itemView.recommendation_tv.text = item.name.toString()

            if (item.image != null){
                requestManager
                    .load(item.image)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.test_image_back)
                    .into(itemView.recommendation_iv)
            }else
                requestManager
                    .load(R.drawable.fragment_appartments_image_default)
                    .transition(withCrossFade())
                    .into(itemView.recommendation_iv)
        }

    }
}