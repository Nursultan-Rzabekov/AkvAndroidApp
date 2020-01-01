package com.example.akvandroidapp.ui.main.profile.add_ad


import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_address.*


class ProfileAddAddressFragment : BaseProfileFragment(){

    private var type: String? = null
    private var guestsCount: Int? = null
    private var roomsCount: Int? = null
    private var bedsCount: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_address, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateContent()

        type = arguments?.getString("typeOfApartment")
        guestsCount = arguments?.getInt("guestsCount")
        roomsCount = arguments?.getInt("roomsCount")
        bedsCount = arguments?.getInt("bedsCount")

        fragment_add_ad_address_next_btn.setOnClickListener {
            if (checkInputs())
                navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navNextFragment(){
        val bundle = bundleOf("bedsCount" to bedsCount, "roomsCount" to roomsCount,
            "guestsCount" to guestsCount, "type" to type, "country" to fragment_add_ad_address_country_et.text.toString(),
            "region" to fragment_add_ad_address_region_et.text.toString(), "city" to fragment_add_ad_address_city_et.text.toString(),
            "address" to fragment_add_ad_address_address_et.text.toString(), "post_index" to fragment_add_ad_address_post_index_et.text.toString())
        findNavController().navigate(R.id.action_pprofileAddAddressFragment_to_profileAddGalleryFragment, bundle)
    }

    private fun initiateContent(){
        setSpans()
    }

    private fun setSpans(){
        val foregroundColor = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primaryZero))

        fragment_add_ad_address_country_tv.setText(fragment_add_ad_address_country_tv.text.toString(), TextView.BufferType.SPANNABLE)
        val span1 = fragment_add_ad_address_country_tv.text as Spannable
        span1.setSpan(foregroundColor, 7, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        fragment_add_ad_address_city_tv.setText(fragment_add_ad_address_city_tv.text.toString(), TextView.BufferType.SPANNABLE)
        val span2 = fragment_add_ad_address_city_tv.text as Spannable
        span2.setSpan(foregroundColor, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        fragment_add_ad_address_address_tv.setText(fragment_add_ad_address_address_tv.text.toString(), TextView.BufferType.SPANNABLE)
        val span3 = fragment_add_ad_address_address_tv.text as Spannable
        span3.setSpan(foregroundColor, 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        fragment_add_ad_address_show_map_btn.setText(fragment_add_ad_address_show_map_btn.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_address_show_map_btn.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_address_show_map_btn.text.toString().length, 0)
    }

    private fun checkInputs(): Boolean{
        if (fragment_add_ad_address_country_et.text.toString().trim() != "" &&
            fragment_add_ad_address_city_et.text.toString().trim() != "" &&
            fragment_add_ad_address_address_et.text.toString().trim() != "")
            return true
        if (fragment_add_ad_address_country_et.text.toString().trim() == "") {
            fragment_add_ad_address_country_l_et.isErrorEnabled = true
            fragment_add_ad_address_country_l_et.error = getString(R.string.invalid)
        }
        if (fragment_add_ad_address_city_et.text.toString().trim() == ""){
            fragment_add_ad_address_city_l_et.isErrorEnabled = true
            fragment_add_ad_address_city_l_et.error = getString(R.string.invalid)
        }
        if (fragment_add_ad_address_address_et.text.toString().trim() == ""){
            fragment_add_ad_address_address_l_et.isErrorEnabled = true
            fragment_add_ad_address_address_l_et.error = getString(R.string.invalid)
        }

        return false
    }
}

















