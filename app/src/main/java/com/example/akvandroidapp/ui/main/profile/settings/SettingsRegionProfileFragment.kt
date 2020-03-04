package com.example.akvandroidapp.ui.main.profile.settings


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterCity
import com.example.akvandroidapp.ui.main.search.filter.FilterCityAdapter
import kotlinx.android.synthetic.main.fragment_region.*
import kotlinx.android.synthetic.main.fragment_region_layout.*
import javax.inject.Inject


class SettingsRegionProfileFragment : BaseProfileFragment(), FilterCityAdapter.CityInteraction {

    private lateinit var regionAdapter: FilterCityAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_region_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsRegionProfileFragment: ${viewModel}")


        setToolbar()
        initRecyclerView()
        addRegionDataset()
    }

    private fun initRecyclerView(){
        fragment_region_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@SettingsRegionProfileFragment.context)
            regionAdapter = FilterCityAdapter(this@SettingsRegionProfileFragment)
            adapter = regionAdapter
        }
    }

    private fun addRegionDataset(){
        val data = mutableListOf(FilterCity("Алматы", false, true, -1),
            FilterCity("Шымкент", false, false, -1),
            FilterCity("Астана", false, false, -1))
        for (region in data) {
            if (sessionManager.settingsInfo.value?._region?.location == region.location){
                region.isSelected = true
            }
        }
        regionAdapter.submitList(data)
    }

    override fun onItemSelected(position: Int, item: FilterCity) {
        sessionManager.setSettingsRegion(item)

//        Log.d("just","just + ${item.location} + ${item.isSelected} + ${item.isMyLocation}")
//        val bundle = bundleOf("city" to item.location)
//        findNavController().navigate(R.id.action_filterCityFragment_to_back_filter,bundle)
        findNavController().navigateUp()
    }

    private fun setToolbar(){
        header_region_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_region_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}