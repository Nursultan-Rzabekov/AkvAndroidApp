package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds_earnings.*
import kotlinx.android.synthetic.main.fragment_settings.*


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

        initRecyclerView()

        earningRecycler.submitList(earnings!!.toList())

//        main_back_img_btn.setOnClickListener {
//            findNavController().navigateUp()
//        }
    }

    private fun initRecyclerView(){
        fragment_my_adds_earnings_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseMoneyProfileFragment.context)
            earningRecycler = EarningsListAdapter()
            adapter = earningRecycler
        }
        earningRecycler.submitList(listOf())
    }
}