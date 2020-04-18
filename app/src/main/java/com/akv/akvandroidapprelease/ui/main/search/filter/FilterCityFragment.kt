package com.akv.akvandroidapprelease.ui.main.search.filter


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.ui.BaseActivity
import com.akv.akvandroidapprelease.util.Constants
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_city_layout.*


class FilterCityFragment : BaseActivity(), FilterCityAdapter.CityInteraction {

    private lateinit var cityAdapter: FilterCityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_city_layout)


        initRecyclerView()
        addCityDataset()
        setToolbar()

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

    override fun expandAppBar() {
    }
    override fun displayProgressBar(bool: Boolean) {
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
            layoutManager = LinearLayoutManager(this@FilterCityFragment)
            cityAdapter = FilterCityAdapter(this@FilterCityFragment)
            adapter = cityAdapter
        }
    }

    override fun onItemSelected(position: Int, item: FilterCity) {
        sessionManager.filterCity(item)
        finish()
    }

    private fun setToolbar(){
        header_city_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back)
        setSupportActionBar(header_city_toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_city_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

}