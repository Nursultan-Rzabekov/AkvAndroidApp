package com.akv.akvandroidapprelease.ui.main.profile.payment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.entity.PaymentHistoryItem
import com.akv.akvandroidapprelease.util.Converters
import kotlinx.android.synthetic.main.payments_history_recycler_view_item.view.*

class PaymentHistoryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PaymentHistoryItem>() {

        override fun areItemsTheSame(oldItem: PaymentHistoryItem, newItem: PaymentHistoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentHistoryItem, newItem: PaymentHistoryItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            BlogRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: PaymentHistoryAdapter
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
        return PaymentHistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.payments_history_recycler_view_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PaymentHistoryViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun submitList(blogList: List<PaymentHistoryItem>?){
        val newList = blogList?.toMutableList()

        val list = differ.currentList.toMutableList()

        newList?.forEach {
            list.add(it)
        }
        differ.submitList(list)
    }

    fun clearAndSubmitList(blogList: List<PaymentHistoryItem>?){
        val newList = blogList?.toMutableList()

        differ.submitList(newList)
    }

    fun clearList(){
        differ.submitList(listOf())
    }

    class PaymentHistoryViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        fun bind(item: PaymentHistoryItem){
            itemView.payments_history_recycler_view_item_nickname_tv.text = item.id.toString()
            itemView.payments_history_recycler_view_item_price_tv.text = ("${Converters.pretifyPrice(item.amout)}kzt")
        }

    }
}