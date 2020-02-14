package com.example.akvandroidapp.ui.main.search.filter


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_type.*
import kotlinx.android.synthetic.main.fragment_type_layout.*
import java.util.*
import javax.inject.Inject


class FilterTypeFragment : BaseActivity(), FilterTypeAdapter.FilterTypeInteraction {

    private lateinit var typeAdapter: FilterTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_type_layout)

        setToolbar()
        initRecyclerView()
        subscribeObservers()
    }

    override fun expandAppBar() {
    }

    override fun displayProgressBar(bool: Boolean) {
    }

    private fun subscribeObservers(){
        sessionManager.typeOfApartment.observe(this, androidx.lifecycle.Observer{
            typeAdapter.submitList(getTypes(it))
        })
    }

    private fun initRecyclerView(){
        Log.e("GroupRai", sessionManager.typeOfApartment.value.toString())
        fragment_type_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FilterTypeFragment)
            typeAdapter = FilterTypeAdapter(this@FilterTypeFragment)
            adapter = typeAdapter
        }
    }

    private fun setToolbar(){
        header_type_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back)
        setSupportActionBar(header_type_toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_type_toolbar.setNavigationOnClickListener{
            finish()
        }
    }

    private fun getTypes(selectedId: Int): List<FilterTypeAdapter.FilterType>{
        val items = mutableListOf<FilterTypeAdapter.FilterType>()
        Constants.mapTypes.forEach{
            items.add(FilterTypeAdapter.FilterType(it.value, it.key, it.value == selectedId))
        }
        return items
    }

    override fun onItemSelected(position: Int, item: FilterTypeAdapter.FilterType) {
        sessionManager.filterTypeOfApartment(item.id)
        finish()
    }

}