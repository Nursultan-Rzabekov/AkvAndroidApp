package com.example.akvandroidapp.ui.main.messages.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.requests_recycler_view_item.view.*
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*

class RequestListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
    ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val BLOG_ITEM = 0

    private val NO_MORE_RESULTS_BLOG_MARKER = HomeReservation(
        NO_MORE_RESULTS,
        "" ,
        "",
        0,
        0,
        "",
        false,
        0,
        0,
        "",
        "",
        0
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HomeReservation>() {

        override fun areItemsTheSame(oldItem: HomeReservation, newItem: HomeReservation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeReservation, newItem: HomeReservation): Boolean {
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
                return OrderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.requests_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return OrderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.requests_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: RequestListAdapter
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
            is OrderViewHolder -> {
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
        list: List<HomeReservation>
    ){

    }


    fun submitList(blogList: List<HomeReservation>?, isQueryExhausted: Boolean){
        val newList = blogList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_BLOG_MARKER)
        differ.submitList(newList)
    }

    class OrderViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        private val image = itemView.requests_recycler_view_item_iv
        private val title = itemView.requests_recycler_view_item_title_tv
        private val nickname = itemView.requests_recycler_view_item_message_tv

        fun bind(item: HomeReservation) = with(itemView) {

            title.text = item.house_name
            nickname.text = ("@${item.user_id} хочет быть вашем гостем.")

            if (item.house_image != null)
                requestManager
                    .load(item.house_image)
                    .transition(withCrossFade())
                    .error(R.drawable.test_image_back)
                    .into(image)
            else
                requestManager
                    .load(R.drawable.test_image_back)
                    .into(image)

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: HomeReservation)
    }

}
