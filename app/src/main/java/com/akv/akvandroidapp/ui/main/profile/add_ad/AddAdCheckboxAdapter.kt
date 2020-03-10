package com.akv.akvandroidapp.ui.main.profile.add_ad

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.akv.akvandroidapp.R
import kotlinx.android.synthetic.main.add_ad_checkbox_recycler_view_item.view.*

class AddAdCheckboxAdapter(



    private val checklInteraction: CheckboxCheckInteraction? = null,
    private val closeInteraction: CheckboxCloseInteraction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CheckboxItem>() {

        override fun areItemsTheSame(oldItem: CheckboxItem, newItem: CheckboxItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: CheckboxItem, newItem: CheckboxItem): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            BlogRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class BlogRecyclerChangeCallback(
        private val adapter: AddAdCheckboxAdapter
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
        return CheckBoxViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_ad_checkbox_recycler_view_item, parent, false),
            checkInteraction = checklInteraction,
            closeInteraction = closeInteraction
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when(holder) {
           is CheckBoxViewHolder -> {
               holder.bind(differ.currentList[holder.adapterPosition])
           }
       }
    }

    fun submitList(items: MutableList<CheckboxItem>){
        val newList = items.toMutableList()
        differ.submitList(newList)
        notifyDataSetChanged()
    }

    fun addItem(text: String, isChecked: Boolean = false){
        val newList = differ.currentList.toMutableList()
        newList.add(CheckboxItem(text, isChecked))
        differ.submitList(newList)
    }

    fun addItems(staticList: List<String>, list: List<String>, isChecked: Boolean = false, isStatic: Boolean = false){
        val checkboxes = mutableListOf<CheckboxItem>()

        for (item in staticList)
            checkboxes.add(
                CheckboxItem(item, isCheked = false, isStatic = true)
            )

        for(item in list) {
            if (item in staticList){
                checkboxes[staticList.indexOf(item)].isCheked = true
            }
            else
                checkboxes.add(CheckboxItem(item, isChecked, isStatic = false))
        }
        differ.submitList(checkboxes)
        Log.e("Checkbox Adapter", "$checkboxes")
    }

    fun addStaticItems(list: List<String>) {
        val newList = mutableListOf<CheckboxItem>()
        for (item in list)
            newList.add(CheckboxItem(item, isCheked = false, isStatic = true))
        differ.submitList(newList)
        Log.e("Checkbox Adapter", "$newList")
    }

    fun removeItem(position: Int) {
        val newList = differ.currentList.toMutableList()
        newList.removeAt(position)
        differ.submitList(newList)
    }

    fun uncheckAll(){
        differ.currentList.forEach { it.isCheked = false }
        notifyDataSetChanged()
    }

    fun isCheckItem(position: Int, isChecked: Boolean){
        differ.currentList[position].isCheked = isChecked
    }

    fun getList(): MutableList<CheckboxItem>{
        return differ.currentList.toMutableList()
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