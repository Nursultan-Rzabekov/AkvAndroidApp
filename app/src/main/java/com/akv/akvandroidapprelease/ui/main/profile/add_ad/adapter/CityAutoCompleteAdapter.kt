package com.akv.akvandroidapprelease.ui.main.profile.add_ad.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.util.CityList
import java.util.*
import kotlin.collections.ArrayList

class CityAutoCompleteAdapter(
    val mContext: Context,
    var items: List<CityList>?,
    var region_id: Int = 0
) : ArrayAdapter<CityList>(mContext, 0, items!!), Filterable {

    private var fullList: ArrayList<CityList>? = null

    override fun getCount(): Int = items!!.size

    override fun getItem(position: Int): CityList? {
        return items!![position]
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
        return cityFilter
    }

    public fun setRegionId(region: Int){
        region_id = region
    }

    private val cityFilter = object: Filter(){
        private val lock = Object()
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filterResults = FilterResults()

            if (fullList == null ){
                synchronized(lock){
                    if (region_id == 0)
                        fullList = ArrayList(items!!.toMutableList())
                    else {
                        val match = ArrayList<CityList>()
                        for (item in items!!)
                            if (item.region_id == region_id)
                                match.add(item)
                        fullList = ArrayList(match)
                    }
                }
            }

            if (p0 == null || p0.length == 0){
                synchronized(lock){
                    filterResults.values = fullList
                    filterResults.count = fullList!!.size
                }
            } else{
                val search = p0.toString().toLowerCase(Locale.getDefault())

                val match = ArrayList<CityList>()

                for (dataItem in fullList!!)
                    if (dataItem.region_id == region_id)
                        if (dataItem.name.toLowerCase(Locale.getDefault()).startsWith(search))
                            match.add(dataItem)

                filterResults.values = match
                filterResults.count = match.size
            }

            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            if (p1?.values != null)
                items = p1.values as List<CityList>
            else
                items = null

            if (p1?.count!! > 0 )
                notifyDataSetChanged()
            else
                notifyDataSetInvalidated()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as CityList).name
        }

    }
}