package com.akv.akvandroidapp.ui.main.profile.add_ad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.util.ReqionList

class RegionAutoCompleteAdapter(
    val mContext: Context,
    var items: List<ReqionList>?
) : ArrayAdapter<ReqionList>(mContext, 0, items!!), Filterable {

    private var fullList: ArrayList<ReqionList>? = null

    override fun getCount(): Int = items!!.size

    override fun getItem(position: Int): ReqionList? {
        return items!!.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (convertView == null)
            view = LayoutInflater.from(context).inflate(
                R.layout.support_simple_spinner_dropdown_item_custom, parent, false
            )

        val textView = view?.findViewById<TextView>(R.id.text_drop)

        if (view != null)
            textView?.text = getItem(position)?.name

        return view!!
    }

    override fun getFilter(): Filter {
        return regionFilter
    }

    private val regionFilter = object : Filter(){
        private val lock = Object()

        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filterResults = FilterResults()

            if (fullList == null ){
                synchronized(lock){
                    fullList = ArrayList(items!!.toMutableList())
                }
            }

            filterResults.values = fullList
            filterResults.count = fullList!!.size

//            if (p0 == null || p0.length == 0){
//                synchronized(lock){
//                    filterResults.values = fullList
//                    filterResults.count = fullList!!.size
//                }
//            } else{
//                val search = p0.toString().toLowerCase(Locale.getDefault())
//
//                val match = ArrayList<ReqionList>()
//
//                for (dataItem in fullList!!)
//                    if (dataItem.name.toLowerCase(Locale.getDefault()).startsWith(search))
//                        match.add(dataItem)
//
//                filterResults.values = match
//                filterResults.count = match.size
//            }

            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            if (p1?.values != null)
                items = p1.values as List<ReqionList>
            else
                items = null

            if (p1?.count!! > 0 )
                notifyDataSetChanged()
            else
                notifyDataSetInvalidated()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as ReqionList).name
        }

    }

}