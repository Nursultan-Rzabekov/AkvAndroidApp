package com.akv.akvandroidapprelease.ui.main.search.filter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.akv.akvandroidapprelease.R
import kotlinx.android.synthetic.main.city_choose_recycler_view_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class FilterCityAdapter(
    private val interaction: CityInteraction? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var cities: MutableList<FilterCity> = ArrayList()
    private var citiesFiltered: MutableList<FilterCity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilterCityViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.city_choose_recycler_view_item, parent, false),
            interaction = interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is FilterCityViewHolder -> {
                holder.bind(citiesFiltered[position])
            }
        }
    }

    override fun getItemCount(): Int = citiesFiltered.size

    fun submitList(cityList: MutableList<FilterCity>){
        cities = cityList
        citiesFiltered = ArrayList(cityList)
        notifyDataSetChanged()
    }


    class FilterCityViewHolder constructor(
        itemView: View,
        private val interaction: CityInteraction?
    ): RecyclerView.ViewHolder(itemView){

        private val city_location = itemView.city_choose_recycler_view_item_location
        private val city_selected = itemView.city_choose_recycler_view_item_selected_iv
        private val city_my_location = itemView.city_choose_recycler_view_item_my_location
        private val city_layout = itemView.city_choose_recycler_view_item_layout

        fun bind(filterCity: FilterCity) {
            city_layout.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, filterCity)
            }

            city_location.text = filterCity.location
            if (filterCity.isSelected) {
                city_location.setTextColor(Color.parseColor("#CD3232"))
                city_selected.visibility = View.VISIBLE
            } else {
                city_location.setTextColor(Color.BLACK)
                city_selected.visibility = View.INVISIBLE
            }

            if (filterCity.isMyLocation) {
                city_my_location.visibility = View.VISIBLE
            } else {
                city_my_location.visibility = View.INVISIBLE
            }

        }

    }

    interface CityInteraction{
        fun onItemSelected(position: Int, item: FilterCity)
    }

    override fun getFilter(): Filter {
        return cityFilter
    }

    private val cityFilter = object: Filter(){
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = ArrayList<FilterCity>()

            if (p0 == null || p0.isEmpty()) {
                filteredList.addAll(cities)
            } else{
                val filterPattern = p0.toString().toLowerCase(Locale.getDefault()).trim()

                for (city in cities) {
                    if (city.location.toLowerCase(Locale.getDefault()).startsWith(filterPattern)) {
                        filteredList.add(city)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList

            return results

        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            citiesFiltered.clear()
            citiesFiltered.addAll(p1?.values as MutableList<FilterCity>)
            notifyDataSetChanged()
        }

    }

}