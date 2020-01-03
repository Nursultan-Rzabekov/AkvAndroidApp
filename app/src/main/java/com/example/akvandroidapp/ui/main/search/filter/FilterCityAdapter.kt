package com.example.akvandroidapp.ui.main.search.filter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.akvandroidapp.R
import kotlinx.android.synthetic.main.city_choose_recycler_view_item.view.*
import java.util.logging.Filter

class FilterCityAdapter(
    private val interaction: CityInteraction? = null,
    private val interactionCheck: CityInteractionCheck? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cities: List<FilterCity> = ArrayList()
    private var chekedPositon = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilterCityViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.city_choose_recycler_view_item, parent, false),
            interaction = interaction,
            interactionCheck = interactionCheck
        )
    }

    fun changePosition(position: Int){
        this.chekedPositon = position
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is FilterCityViewHolder -> {
                holder.bind(cities.get(position))
                holder.city_layout.setOnClickListener {
                    changePosition(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int = cities.size

    fun submitList(cityList: List<FilterCity>){
        cities = cityList
        notifyDataSetChanged()
    }


    class FilterCityViewHolder constructor(
        cityView: View,
        private val interaction: CityInteraction?,
        private val interactionCheck: CityInteractionCheck?
    ): RecyclerView.ViewHolder(cityView){

        val city_location = cityView.city_choose_recycler_view_item_location
        val city_selected = cityView.city_choose_recycler_view_item_selected_iv
        val city_my_location = cityView.city_choose_recycler_view_item_my_location
        val city_layout = cityView.city_choose_recycler_view_item_layout

        fun bind(filterCity: FilterCity) = with(itemView){
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, filterCity)
            }

            city_location.text = filterCity.location
            if (filterCity.isSelected) {
                city_location.setTextColor(Color.parseColor("#CD3232"))
                city_selected.visibility = View.VISIBLE
            }

            if (filterCity.isMyLocation) {
                city_my_location.visibility = View.VISIBLE
            }

        }

    }

    interface CityInteraction{
        fun onItemSelected(position: Int, item: FilterCity)
    }

    interface CityInteractionCheck{
        fun onItemSelected(position: Int, item: FilterCity,boolean: Boolean)
    }

}