package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.session.HouseUpdateData
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_my_adds_change.*
import kotlinx.android.synthetic.main.fragment_my_adds_detailed.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.header_my_adds_change.*
import javax.inject.Inject


class MyHouseDetailEditProfileFragment : BaseProfileFragment(), GalleryPhotosAdapter.PhotoCloseInteraction {

    private lateinit var photosAdapter: GalleryPhotosAdapter

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adds_change_layout, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        initRecyclerView()

        Log.d(TAG, "MyHouseDetailEditProfileFragment: ${viewModel}")

        setObservers()

        header_my_adds_change_cancel.setOnClickListener {
            sessionManager.clearHouseUpdateData()
            findNavController().navigateUp()
        }

        header_my_adds_change_save.setOnClickListener {
            sessionManager.setHouseUpdateData(
                HouseUpdateData(
                    -1,
                    fragment_my_adds_change_title_et.text.toString().trim(),
                    fragment_my_adds_change_desc_et.text.toString().trim(),
                    photosAdapter.getPhotos(),
                    price = fragment_my_adds_change_price_et.text.toString().toIntOrNull(),
                    address = fragment_my_adds_change_address_et.text.toString().trim())
            )
        }

        fragment_my_adds_change_rules.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseRulesEditProfileFragment)
        }


        fragment_my_adds_change_facilities.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseFacilitiesEditProfileFragment)
        }


        fragment_my_adds_change_near.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseNearEditProfileFragment)
        }


        fragment_my_adds_change_available_dates.setOnClickListener {
            findNavController().navigate(R.id.action_myHouseDetailEditProfileFragment_to_myHouseAvailableDatesEditProfileFragment)
        }

    }

    private fun initRecyclerView(){
        fragment_my_adds_change_photos_recycler_view.apply {
            layoutManager = LinearLayoutManager(
                this@MyHouseDetailEditProfileFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false)
            photosAdapter = GalleryPhotosAdapter(requestManager, this@MyHouseDetailEditProfileFragment)
            adapter = photosAdapter
        }
    }

    private fun setObservers() {
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, Observer {
            fragment_my_adds_change_title_et.setText(it.title.toString())
            fragment_my_adds_change_desc_et.setText(it.description.toString())
            fragment_my_adds_change_price_et.setText(it.price.toString())
            fragment_my_adds_change_address_et.setText(it.address.toString())
            fragment_my_adds_change_photos_tv.text = ("${it.photosList.size}/15")
            photosAdapter.submitList(it.photosList)
        })
    }

    override fun onItemClosed(position: Int, item: String?) {
        photosAdapter.removeItem(position)
        fragment_my_adds_change_photos_tv.text = ("${photosAdapter.itemCount}/15")
    }
}