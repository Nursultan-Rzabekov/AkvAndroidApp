package com.akv.akvandroidapprelease.ui.main.search.filter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.akv.akvandroidapprelease.R
import kotlinx.android.synthetic.main.filter_typeof_recycler_view_item.view.*

class FilterTypeAdapter(
    private val interaction: FilterTypeInteraction
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FilterType>() {

        override fun areItemsTheSame(oldItem: FilterType, newItem: FilterType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FilterType, newItem: FilterType): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            BlogRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: FilterTypeAdapter
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
        return FilterTypeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.filter_typeof_recycler_view_item,
                parent,
                false
            ),
            interaction = interaction
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is FilterTypeViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    fun submitList(items: List<FilterType>){
        differ.submitList(items)
    }

    class FilterTypeViewHolder(
        itemView: View,
        val interaction: FilterTypeInteraction?
    ): RecyclerView.ViewHolder(itemView){

        fun bind(item: FilterType){
            itemView.fragment_type_tv.text = item.title

            when(item.isSelected){
                true -> {
                    itemView.fragment_type_tv.setTextColor(Color.parseColor("#CD3232"))
                    itemView.fragment_type_checked_rbtn.isChecked = true
                }

                false -> {
                    itemView.fragment_type_tv.setTextColor(Color.BLACK)
                    itemView.fragment_type_checked_rbtn.isChecked = false
                }
            }

            itemView.filter_type_layout.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
        }

    }

    data class FilterType(
        var id: Int,
        var title: String,
        var isSelected: Boolean
    )

    interface FilterTypeInteraction{
        fun onItemSelected(position: Int, item: FilterType)
    }

}