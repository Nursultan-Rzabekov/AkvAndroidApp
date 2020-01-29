package com.example.akvandroidapp.ui.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.book_requests_recycler_view_item.view.*
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*

class HomeListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val RESERVATION_ITEM = 0
    private val NO_RESULTS = -2

    private val NO_MORE_RESULTS_RESERVATION_MARKER = HomeReservation(
        NO_MORE_RESULTS,
        "" ,
        "",
        0,
        0,
        "",
        false,
        -1,
        -1,
        "",
        "",
        -1
    )

    private val NO_RESULTS_RESERVATION_MARKER = HomeReservation(
        NO_RESULTS,
        "" ,
        "",
        0,
        0,
        "",
        false,
        -1,
        -1,
        "",
        "",
        -1
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
            NO_RESULTS -> {
                Log.e(TAG, "onCreateViewHolder: No results at all...")
                return NoMoreReservationViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.book_request_no_results_recycler_view_item,
                        parent,
                        false
                    )
                )
            }

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

            RESERVATION_ITEM ->{
                return ReservationViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.book_requests_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return ReservationViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.book_requests_recycler_view_item,
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
        private val adapter: HomeListAdapter
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
            is ReservationViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList[position].id > -1){
            return RESERVATION_ITEM
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
        for(reserv in list){
            requestManager
                .load(reserv.house_image)
                .error(R.drawable.test_image_back)
                .preload()

        }
    }

    fun submitList(blogList: List<HomeReservation>?, isQueryExhausted: Boolean) {

        Log.e("HOME FRAGMENT adapter", "${blogList}")
        if (!blogList.isNullOrEmpty()) {
            val newList = blogList.toMutableList()
            Log.e("home adapter list", "$newList")
            if (isQueryExhausted)
                newList.add(NO_MORE_RESULTS_RESERVATION_MARKER)
            differ.submitList(newList)
        }else{
            differ.submitList(mutableListOf(NO_RESULTS_RESERVATION_MARKER))
        }
    }

    class ReservationViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HomeReservation) = with(itemView) {

            itemView.book_requests_recycler_view_item_title_tv.text = item.house_name

            when(item.status){
                0 -> {
                    itemView.let {
                        book_requests_recycler_view_item_processing_layout.visibility = View.VISIBLE
                        book_requests_recycler_view_item_accepted_layout.visibility = View.GONE
                        book_requests_recycler_view_item_canceled_layout.visibility = View.GONE
                    }
                }
                1 -> {
                    itemView.let {
                        book_requests_recycler_view_item_processing_layout.visibility = View.GONE
                        book_requests_recycler_view_item_accepted_layout.visibility = View.VISIBLE
                        book_requests_recycler_view_item_canceled_layout.visibility = View.GONE
                    }
                }
                2 -> {
                    itemView.let {
                        book_requests_recycler_view_item_processing_layout.visibility = View.GONE
                        book_requests_recycler_view_item_accepted_layout.visibility = View.GONE
                        book_requests_recycler_view_item_canceled_layout.visibility = View.VISIBLE
                    }
                }
                else -> {
                    itemView.let {
                        book_requests_recycler_view_item_processing_layout.visibility = View.VISIBLE
                        book_requests_recycler_view_item_accepted_layout.visibility = View.GONE
                        book_requests_recycler_view_item_canceled_layout.visibility = View.GONE
                    }
                }
            }

            requestManager
                .load(item.house_image)
                .error(R.drawable.test_image_back)
                .into(itemView.book_requests_recycler_view_item_iv)
        }
    }

    class NoMoreReservationViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView)

    interface Interaction {
        fun onItemSelected(position: Int, item: HomeReservation)
    }

}
