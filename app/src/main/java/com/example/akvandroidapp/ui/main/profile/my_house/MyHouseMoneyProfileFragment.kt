package com.example.akvandroidapp.ui.main.profile.my_house


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
import com.example.akvandroidapp.ui.main.profile.my_house.adapters.EarningsListAdapter
import com.example.akvandroidapp.ui.main.profile.my_house.adapters.HouseEarning
import com.example.akvandroidapp.util.Converters
import kotlinx.android.synthetic.main.fragment_my_adds_earnings.*
import kotlinx.android.synthetic.main.fragment_my_adds_earnings_layout.*


class MyHouseMoneyProfileFragment : BaseMyHouseFragment() {

    private lateinit var earningRecycler: EarningsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_earnings_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")

        val earnings = arguments?.getParcelableArrayList<HouseEarning>("earnings")

        setToolbar()
        initRecyclerView()

        fragment_my_adds_earnings_total_tv.text =
            ("+${Converters.pretifyPrice(getTotalIncome(earnings!!.toList()))}kzt")

        earningRecycler.submitList(earnings.toList())
        Log.e("MyHouseMoneyProfile", "$earnings")
    }

    private fun initRecyclerView(){
        fragment_my_adds_earnings_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseMoneyProfileFragment.context)
            earningRecycler =
                EarningsListAdapter()
            adapter = earningRecycler
        }
    }

    private fun setToolbar(){
        fragment_my_adds_earnings_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_my_adds_earnings_toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }
    }

    private fun getTotalIncome(earnings: List<HouseEarning>): Int{
        var total = 0
        for (earning in earnings)
            total += earning.earning
        return total
    }
}