package com.example.akvandroidapp.ui.main.search.filter


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_city.*


class FilterCityFragment : BaseSearchFragment(), FilterCityAdapter.CityInteraction, FilterCityAdapter.CityInteractionCheck {

    private lateinit var cityAdapter: FilterCityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        initRecyclerView()
        addCityDataset()

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun addCityDataset(){
        val data = listOf(FilterCity("Almaty, Kazakhstan", true, true),
                            FilterCity("Astana, Kazakhstan", false, false),
                            FilterCity("Uralsk, Kazakhstan", false, false),
                            FilterCity("asd, Kazakhstan", false, false))
        cityAdapter.submitList(data)
    }

    private fun initRecyclerView(){
        fragment_city_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FilterCityFragment.context)
            cityAdapter = FilterCityAdapter()
            adapter = cityAdapter
        }
    }

    override fun onItemSelected(position: Int, item: FilterCity) {
        findNavController().navigateUp()
    }

    override fun onItemSelected(position: Int, item: FilterCity, boolean: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}