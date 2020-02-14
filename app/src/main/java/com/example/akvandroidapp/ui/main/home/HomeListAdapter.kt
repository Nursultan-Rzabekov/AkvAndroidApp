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
import kotlinx.android.synthetic.main.book_request_no_results_recycler_view_item.view.*
import kotlinx.android.synthetic.main.book_requests_accepted_recycler_view_item.view.*
import kotlinx.android.synthetic.main.book_requests_canceled_recycler_view_item.view.*
import kotlinx.android.synthetic.main.book_requests_recycler_view_item.view.*
import kotlinx.android.synthetic.main.search_result_recycler_item.view.*

class HomeListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val RESERVATION_ITEM_DEFAULT = 0
    private val RESERVATION_ITEM_ACCEPTED = 1
    private val RESERVATION_ITEM_CANCELED = 2
    private val NO_RESULTS = -2

    private val NO_MORE_RESULTS_RESERVATION_MARKER = HomeReservation(
        NO_MORE_RESULTS,
        "" ,
        "",
        0,
        0,
        "",
        null,
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
        null,
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
                    ),
                    interaction = interaction
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

            RESERVATION_ITEM_DEFAULT ->{
                return ReservationDefaultViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.book_requests_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }

            RESERVATION_ITEM_CANCELED -> {
                return ReservationCanceledViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.book_requests_canceled_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }

            RESERVATION_ITEM_ACCEPTED -> {
                return ReservationAcceptedViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.book_requests_accepted_recycler_view_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }

            else -> {
                return ReservationDefaultViewHolder(
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
            is ReservationDefaultViewHolder -> {
                holder.bind(differ.currentList[position])
            }

            is ReservationAcceptedViewHolder -> {
                holder.bind(differ.currentList[position])
            }

            is ReservationCanceledViewHolder -> {
                holder.bind(differ.currentList[position])
            }

            is NoMoreReservationViewHolder ->{
                holder.bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(differ.currentList[position].id > -1){
            return when (differ.currentList[position].accepted_house){
                true -> RESERVATION_ITEM_ACCEPTED
                false -> RESERVATION_ITEM_CANCELED
                null -> RESERVATION_ITEM_DEFAULT
            }
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

    fun clearAndSubmitList(blogList: List<HomeReservation>?, isQueryExhausted: Boolean) {
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

    fun submitList(blogList: List<HomeReservation>?, isQueryExhausted: Boolean) {
        Log.e("HOME FRAGMENT adapter", "${blogList}")
        val newList = blogList?.toMutableList()
        Log.e("home adapter list", "$newList")
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_RESERVATION_MARKER)

        val currentList = differ.currentList.toMutableList()

        currentList.removeAll { it.id == NO_RESULTS || it.id == NO_MORE_RESULTS }

        newList?.forEach {
            currentList.add(it)
        }

        if (currentList.isNullOrEmpty())
            currentList.add(NO_RESULTS_RESERVATION_MARKER)

        differ.submitList(currentList)
    }

    class ReservationDefaultViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HomeReservation) = with(itemView) {

            itemView.book_requests_recycler_view_item_title_tv.text = item.house_name.toString()

            requestManager
                .load(item.house_image)
                .error(R.drawable.test_image_back)
                .into(itemView.book_requests_recycler_view_item_iv)

            itemView.book_requests_recycler_view_item_top.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }
    }

    class ReservationAcceptedViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HomeReservation) = with(itemView) {

            itemView.book_requests_accepted_recycler_view_item_title_tv.text = item.house_name.toString()

            requestManager
                .load(item.house_image)
                .error(R.drawable.test_image_back)
                .into(itemView.book_requests_accepted_recycler_view_item_iv)

            if(item.status == 1){
                book_requests_accepted_recycler_view_item_accepted_lb1.setTextColor(resources.getColor(R.color.primaryZero))
                book_requests_accepted_recycler_view_item_accepted_lb1.text = resources.getString(R.string.request_сanceled_by_user)
                itemView.book_requests_recycler_view_item_accept_btn.visibility = View.GONE
                itemView.book_requests_recycler_view_item_cancel_btn.visibility = View.GONE
            }
            if(item.status == 2){
                book_requests_accepted_recycler_view_item_accepted_lb1.setTextColor(resources.getColor(R.color.primaryZero))
                book_requests_accepted_recycler_view_item_accepted_lb1.text = resources.getString(R.string.request_expired)
                itemView.book_requests_recycler_view_item_accept_btn.visibility = View.GONE
                itemView.book_requests_recycler_view_item_cancel_btn.visibility = View.GONE
            }


            itemView.book_requests_accepted_recycler_view_item_top.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.book_requests_recycler_view_item_cancel_btn.setOnClickListener {
                interaction?.onItemCancelReserv(adapterPosition, item)
            }

            itemView.book_requests_recycler_view_item_accept_btn.setOnClickListener {
                interaction?.onItemPay(adapterPosition, item)
            }
        }
    }

    class ReservationCanceledViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: HomeReservation) = with(itemView) {

            itemView.book_requests_canceled_recycler_view_item_title_tv.text = item.house_name.toString()

            requestManager
                .load(item.house_image)
                .error(R.drawable.test_image_back)
                .into(itemView.book_requests_canceled_recycler_view_item_iv)
        }
    }

    class NoMoreReservationViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ): RecyclerView.ViewHolder(itemView) {

        fun bind() {
            itemView.fragment_saved_booking_book_more_btn.setOnClickListener {
                interaction?.onBookMoreBtnPressed()
            }
        }

    }

    interface Interaction {
        fun onItemSelected(position: Int, item: HomeReservation)
        fun onItemPay(position: Int,item: HomeReservation)
        fun onItemCancelReserv(position: Int,item: HomeReservation)
        fun onBookMoreBtnPressed()
    }

}
