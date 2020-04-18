package com.akv.akvandroidapprelease.ui.main.profile.my_house.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.util.DateUtils
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.earnings_recycler_view_item.view.*
import java.text.DecimalFormat
import java.util.*

class EarningsListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object: DiffUtil.ItemCallback<HouseEarning>(){
        override fun areItemsTheSame(oldItem: HouseEarning, newItem: HouseEarning): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: HouseEarning, newItem: HouseEarning): Boolean {
            return false
        }
    }

    private val differ =
        AsyncListDiffer(
            BlogRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: EarningsListAdapter
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
        return EarningViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.earnings_recycler_view_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is EarningViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun submitList(earning: List<HouseEarning>){
        differ.submitList(earning)
    }

    class EarningViewHolder(
        earningView: View
    ): RecyclerView.ViewHolder(earningView) {

        private val nickname = earningView.earnings_recycler_view_item_nickname_tv
        private val income = earningView.earnings_recycler_view_item_price_tv
        private val date = earningView.earnings_recycler_view_item_date_tv

        fun bind(item: HouseEarning){
            val formatter = DecimalFormat("#,###")
            nickname.text = ("@${item.nickname}")
            income.text = ("+${formatter.format(item.earning)}kzt")
            date.text = DateUtils.convertDateToStringWithSlash(item.date)
        }

    }
}

@Parcelize
data class HouseEarning(
    var id: Int,
    var nickname: String,
    var earning: Int,
    var date: Date
): Parcelable