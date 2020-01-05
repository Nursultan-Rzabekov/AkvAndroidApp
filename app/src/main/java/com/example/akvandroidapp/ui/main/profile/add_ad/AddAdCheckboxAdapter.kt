package com.example.akvandroidapp.ui.main.profile.add_ad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.add_ad_checkbox_recycler_view_item.view.*

class AddAdCheckboxAdapter(
    private val checklInteraction: CheckboxCheckInteraction? = null,
    private val closeInteraction: CheckboxCloseInteraction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CheckBoxViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_ad_checkbox_recycler_view_item, parent, false),
            checkInteraction = checklInteraction,
            closeInteraction = closeInteraction
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder) {
           is CheckBoxViewHolder -> {
               holder.bind(items[position])
           }
       }
    }

    fun submitList(items: MutableList<String>){
        this.items = items
        notifyDataSetChanged()
    }

    class CheckBoxViewHolder constructor(
        itemView: View,
        private val checkInteraction: CheckboxCheckInteraction?,
        private val closeInteraction: CheckboxCloseInteraction?
    ) : RecyclerView.ViewHolder(itemView) {
        private val checkbox = itemView.add_ad_checkbox_recycler_view_item_chkbox
        private val closeIv = itemView.add_ad_checkbox_recycler_view_item_close_iv

        fun bind(text: String) {
            checkbox.text = text

            checkbox.setOnCheckedChangeListener { compoundButton, b ->
                checkInteraction?.onItemChecked(adapterPosition, text, b)
            }

            closeIv.setOnClickListener {
                closeInteraction?.onItemClosed(adapterPosition, text)
            }
        }
    }

    interface CheckboxCheckInteraction{
        fun onItemChecked(position: Int, item: String, checked: Boolean)
    }

    interface CheckboxCloseInteraction{
        fun onItemClosed(position: Int, item: String)
    }
}