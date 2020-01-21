package com.example.akvandroidapp.ui.main.search.filter


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.header_city.*
import javax.inject.Inject


class FilterCityFragment : BaseSearchFragment(), FilterCityAdapter.CityInteraction {

    private lateinit var cityAdapter: FilterCityAdapter

    @Inject
    lateinit var sessionManager: SessionManager

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

        header_city_et.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                fragment_city_recycler_view.invalidate()
                cityAdapter.filter.filter(p0)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    private fun addCityDataset(){
        val data = mutableListOf<FilterCity>()
        for(city in Constants.cityList)
            data.add(FilterCity(city.name, false, false, city.id))
        for (city in data) {
            if (sessionManager.checkedFilterCity.value?.location == city.location){
                city.isSelected = true
            }
        }
        cityAdapter.submitList(data)
    }


    private fun initRecyclerView(){
        fragment_city_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FilterCityFragment.context)
            cityAdapter = FilterCityAdapter(this@FilterCityFragment)
            adapter = cityAdapter
        }
    }

    override fun onItemSelected(position: Int, item: FilterCity) {
        sessionManager.filterCity(item)
        
//        Log.d("just","just + ${item.location} + ${item.isSelected} + ${item.isMyLocation}")
//        val bundle = bundleOf("city" to item.location)
//        findNavController().navigate(R.id.action_filterCityFragment_to_back_filter,bundle)
        findNavController().navigateUp()
    }

}