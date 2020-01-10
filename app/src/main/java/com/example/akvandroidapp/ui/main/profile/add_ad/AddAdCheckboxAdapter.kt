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

    private var items: MutableList<CheckboxItem> = ArrayList()

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
               holder.bind(items[holder.adapterPosition])
           }
       }
    }

    fun submitList(items: MutableList<CheckboxItem>){
        this.items = items
        notifyDataSetChanged()
    }

    fun addItem(text: String, isChecked: Boolean = false){
        items.add(CheckboxItem(text, isChecked))
        notifyItemChanged(items.size - 1)
    }

    fun addAllItems(list: List<String>, isChecked: Boolean = false, isStatic: Boolean = false){
        val start = items.size
        val listOfTitles = mutableListOf<String>()
        for (item in items) listOfTitles.add(item.title)
        for(item in list) {
            if (item in listOfTitles){
                items[listOfTitles.indexOf(item)].isCheked = isChecked
                items[listOfTitles.indexOf(item)].isStatic = true
            }
            else
                items.add(CheckboxItem(item, isChecked, isStatic))
        }
        notifyItemRangeChanged(start, items.size - 1)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun uncheckAll(){
        items.forEach { it.isCheked = false }
        notifyDataSetChanged()
    }

    fun getList(): MutableList<String>{
        val titles = mutableListOf<String>()
        for(item in items)
            titles.add(item.title)
        return titles
    }

    class CheckBoxViewHolder constructor(
        itemView: View,
        private val checkInteraction: CheckboxCheckInteraction?,
        private val closeInteraction: CheckboxCloseInteraction?
    ) : RecyclerView.ViewHolder(itemView) {
        private val checkbox = itemView.add_ad_checkbox_recycler_view_item_chkbox
        private val closeIv = itemView.add_ad_checkbox_recycler_view_item_close_iv

        fun bind(checkboxItem: CheckboxItem) {
            checkbox.text = checkboxItem.title

            closeIv.visibility =
                if (checkboxItem.isStatic) View.GONE else View.VISIBLE

            checkbox.setOnCheckedChangeListener { compoundButton, b ->
                checkInteraction?.onItemChecked(adapterPosition, checkboxItem.title.trim(), b)
            }

            closeIv.setOnClickListener {
                closeInteraction?.onItemClosed(adapterPosition, checkboxItem.title.trim())
            }

            checkbox.isChecked = checkboxItem.isCheked
        }
    }

    interface CheckboxCheckInteraction{
        fun onItemChecked(position: Int, item: String, checked: Boolean)
    }

    interface CheckboxCloseInteraction{
        fun onItemClosed(position: Int, item: String)
    }
}