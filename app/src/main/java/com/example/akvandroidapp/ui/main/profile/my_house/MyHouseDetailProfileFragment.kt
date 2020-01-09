package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds_detailed.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.my_adds_recycler_view_item.view.*


class MyHouseDetailProfileFragment : BaseProfileFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_detailed_layout, container, false)
    }

    private var argument:BlogPost? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SettingsProfileFragment: ${viewModel}")

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        argument = arguments?.getParcelable("item")

        fragment_my_adds_detailed_title_tv.text = argument?.name

        requestManager
            .load(argument?.image)
            .error(R.drawable.test_image_back)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(fragment_my_adds_detailed_iv)



        earning.setOnClickListener {
            navNextFragment()
        }

        fragment_my_adds_detailed_change_btn.setOnClickListener {
            navNextDetailEditFragment()
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileMyHouseDetailProfileFragment_to_myHouseMoneyProfileFragment)
    }

    private fun navNextDetailEditFragment(){
        findNavController().navigate(R.id.action_profileMyHouseDetailProfileFragment_to_myHouseDetailEditProfileFragment)
    }

}