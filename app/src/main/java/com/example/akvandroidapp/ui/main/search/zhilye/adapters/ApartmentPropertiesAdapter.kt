package com.example.akvandroidapp.ui.main.search.zhilye.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.ZhilyeDetailProperties
import com.example.akvandroidapp.util.GenericViewHolder
import kotlinx.android.synthetic.main.zhilye_udopstva_near_recycler_view_item.view.*

class ApartmentPropertiesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private var properties : MutableList<ZhilyeDetailProperties> = ArrayList()
    private val NO_PROPERTIES = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            NO_PROPERTIES -> {
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.not_mentioned_recyclerview_item,
                        parent,
                        false
                    )
                )
            }

            else -> {
                return PropertyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.zhilye_udopstva_near_recycler_view_item,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = properties.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PropertyViewHolder -> {
                holder.bind(properties[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (properties[position].id > 0)
            return 0
        return NO_PROPERTIES
    }

    fun submitList(list: List<ZhilyeDetailProperties>){
        if (list.isNotEmpty())
            properties = list.toMutableList()
        else properties = mutableListOf(ZhilyeDetailProperties(-1, null))
        notifyDataSetChanged()
    }

    class PropertyViewHolder(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        fun bind(item: ZhilyeDetailProperties) = with(itemView){
            itemView.zhilye_properties_tv.text = item.name.toString()
        }
    }
}