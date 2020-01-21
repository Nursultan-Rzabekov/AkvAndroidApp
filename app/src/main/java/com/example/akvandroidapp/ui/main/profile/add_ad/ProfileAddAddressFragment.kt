package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.adapter.CityAutoCompleteAdapter
import com.example.akvandroidapp.ui.main.profile.add_ad.adapter.RegionAutoCompleteAdapter
import com.example.akvandroidapp.util.CityList
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.ReqionList
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_address.*
import javax.inject.Inject


class ProfileAddAddressFragment : BaseProfileFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    private var region_id = 0
    private var city_id = 0

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
        initDropDown()

        fragment_add_ad_address_next_btn.setOnClickListener {
            if (checkInputs())
                navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navNextFragment(){
        sessionManager.setAddAdAddressList(
            1,
            region_id,
            city_id,
            fragment_add_ad_address_address_et.text.toString().trim())
            //fragment_add_ad_address_post_index_et.text.toString().trim())
        findNavController().navigate(R.id.action_pprofileAddAddressFragment_to_profileAddGalleryFragment)
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
            fragment_add_ad_address_address_et.text.toString().trim() != ""){
            fragment_add_ad_address_country_l_et.isErrorEnabled = false
            fragment_add_ad_address_city_l_et.isErrorEnabled = false
            fragment_add_ad_address_address_l_et.isErrorEnabled = false
            return true
        }
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

    private fun initDropDown() {
        val countriesAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item_custom,
            Constants.countryList)
        fragment_add_ad_address_country_et.setAdapter(countriesAdapter)

        val regionAdapter : ArrayAdapter<ReqionList> = RegionAutoCompleteAdapter(
            requireContext(),
            Constants.regionList
        )
        fragment_add_ad_address_region_et.setAdapter(regionAdapter)

        var cityAdapter : ArrayAdapter<CityList> = CityAutoCompleteAdapter(
            requireContext(),
            Constants.cityList
        )
        fragment_add_ad_address_city_et.setAdapter(cityAdapter)

        fragment_add_ad_address_region_et.setOnItemClickListener { adapterView, view, i, l ->
            val region = (adapterView.adapter.getItem(i) as ReqionList)
            val citiesFiltered = mutableListOf<CityList>()
            for (reg in Constants.cityList)
                if (reg.region_id == region.id)
                    citiesFiltered.add(reg)
            cityAdapter = CityAutoCompleteAdapter(
                requireContext(),
                citiesFiltered.toList()
            )
            fragment_add_ad_address_city_et.setText("")
            fragment_add_ad_address_city_et.setAdapter(cityAdapter)

            region_id = region.id
            Log.e("ADD ADDRESS ", "$region")
        }

        fragment_add_ad_address_city_et.setOnItemClickListener { adapterView, view, i, l ->
            val city = (adapterView.adapter.getItem(i) as CityList)
            city_id = city.id
        }
    }
}

















