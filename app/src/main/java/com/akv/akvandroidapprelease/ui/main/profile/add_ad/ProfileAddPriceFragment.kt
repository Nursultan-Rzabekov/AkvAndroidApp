package com.akv.akvandroidapprelease.ui.main.profile.add_ad


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.util.Converters
import com.akv.akvandroidapprelease.util.MoneyTextWatcher
import kotlinx.android.synthetic.main.fragment_add_ad_price.*
import java.util.*
import javax.inject.Inject


class ProfileAddPriceFragment : BaseAddHouseFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    private var is7DaysDiscountSelected = false
    private var is30DaysDiscountSelected = false

    private var discountOn7Days = 0
    private var discountOn30Days = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            sessionManager.clearAddAdPriceAndDiscounts()
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Locale.setDefault(Locale.forLanguageTag("ru"))

        setToolbar()
        initialState()
        setObservers()

        fragment_add_ad_price_next_btn.setOnClickListener {
            savePrice()
            navNextFragment()
        }

        fragment_add_ad_price_add_discount_btn.setOnClickListener {
            onGiveDiscount()
        }

        fragment_add_ad_price_remove_btn.setOnClickListener {
            onCancelDiscount()
        }

        fragment_add_ad_price_add_btn.setOnClickListener {
            if (fragment_add_ad_price_seekbar.progress > 0)
                onNewDiscountAdded()
        }

        fragment_add_ad_price_seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                fragment_add_ad_price_discounts_slider_tv.text = ("-$progress%")
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {

            }
            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        fragment_add_ad_price_chip_7_days.setOnCloseIconClickListener {
            remove7Discount()
            onChipClose()
        }

        fragment_add_ad_price_chip_30_days.setOnCloseIconClickListener {
            remove30Discount()
            onChipClose()
        }

        fragment_add_ad_price_discounts_add_7_days_rbtn.setOnClickListener {
            fragment_add_ad_price_one_option_tv.text = ("Кол-во дней: 30 дней")
        }

        fragment_add_ad_price_discounts_add_30_days_rbtn.setOnClickListener {
            fragment_add_ad_price_one_option_tv.text = ("Кол-во дней: 7 дней")
        }

        fragment_add_ad_price_et.addTextChangedListener(MoneyTextWatcher(fragment_add_ad_price_et))
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddPriceFragment_to_profileAddPostFragment)
    }

    private fun initialState(){
        fragment_add_ad_price_discounts_layout.visibility = View.GONE
        fragment_add_ad_price_chip_7_days.visibility = View.GONE
        fragment_add_ad_price_chip_30_days.visibility = View.GONE
        fragment_add_ad_price_discounts_add_layout.visibility = View.GONE
        fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.GONE
        fragment_add_ad_price_discounts_add_choose_days.visibility = View.VISIBLE
        fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
        fragment_add_ad_price_seekbar.progress = 0
    }

    private fun setObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer { dataSate ->
            fragment_add_ad_price_et.setText(dataSate._addAdPrice.toString())
            if (dataSate._addAd7DaysDiscount != 0){
                add7Discount(dataSate._addAd7DaysDiscount)
            }
            if (dataSate._addAd30DaysDiscount != 0){
                add30Discount(dataSate._addAd30DaysDiscount)
            }
        })
    }

    private fun savePrice(){
        Log.e("Add ad discount", "$discountOn7Days and $discountOn30Days")
        sessionManager.setAddAdPriceAndDiscounts(
            Converters.formatPriceToInt(fragment_add_ad_price_et.text.toString()),
            discountOn7Days,
            discountOn30Days)
    }

    private fun onChipClose(){
        if (is7DaysDiscountSelected xor is30DaysDiscountSelected){
            fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.VISIBLE
            fragment_add_ad_price_discounts_add_choose_days.visibility = View.GONE

            if (is7DaysDiscountSelected)
                fragment_add_ad_price_one_option_tv.text = ("Кол-во дней: 30 дней")
            else if (is30DaysDiscountSelected)
                fragment_add_ad_price_one_option_tv.text = ("Кол-во дней: 7 дней")

            fragment_add_ad_price_discounts_layout.visibility = View.VISIBLE
        }
        else if (!is7DaysDiscountSelected && !is30DaysDiscountSelected){
            fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.GONE
            fragment_add_ad_price_discounts_add_choose_days.visibility = View.VISIBLE
            fragment_add_ad_price_discounts_layout.visibility = View.GONE
        }

        if (fragment_add_ad_price_discounts_add_layout.visibility == View.VISIBLE){
            fragment_add_ad_price_add_discount_btn.visibility = View.GONE
        }else
            fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
    }

    private fun onGiveDiscount() {
        if (fragment_add_ad_price_discounts_add_layout.visibility == View.GONE) {
            fragment_add_ad_price_discounts_add_layout.visibility = View.VISIBLE
            fragment_add_ad_price_add_discount_btn.visibility = View.GONE

            if (is7DaysDiscountSelected xor is30DaysDiscountSelected) {
                fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.VISIBLE
                fragment_add_ad_price_discounts_add_choose_days.visibility = View.GONE
            } else {
                fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.GONE
                fragment_add_ad_price_discounts_add_choose_days.visibility = View.VISIBLE
            }
        }
    }

    private fun onCancelDiscount() {
        fragment_add_ad_price_discounts_add_layout.visibility = View.GONE
        fragment_add_ad_price_seekbar.progress = 0
        fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
    }

    private fun onNewDiscountAdded() {
        fragment_add_ad_price_discounts_add_layout.visibility = View.GONE

        if (!(is7DaysDiscountSelected xor is30DaysDiscountSelected)) {
            when (fragment_add_ad_price_discounts_radio_group.checkedRadioButtonId) {
                fragment_add_ad_price_discounts_add_7_days_rbtn.id ->
                    add7Discount(fragment_add_ad_price_seekbar.progress)
                fragment_add_ad_price_discounts_add_30_days_rbtn.id ->
                    add30Discount(fragment_add_ad_price_seekbar.progress)
            }
        } else {
            if (is7DaysDiscountSelected)
                add30Discount(fragment_add_ad_price_seekbar.progress)
            else if (is30DaysDiscountSelected)
                add7Discount(fragment_add_ad_price_seekbar.progress)
        }
    }

    private fun add7Discount(discount: Int){
        is7DaysDiscountSelected = true
        fragment_add_ad_price_chip_7_days.visibility = View.VISIBLE
        fragment_add_ad_price_chip_7_days.text =
            ("Скидка $discount% на 7 дней")
        discountOn7Days = discount
        onDiscountAdd()
    }

    private fun remove7Discount(){
        fragment_add_ad_price_chip_7_days.visibility = View.GONE
        discountOn7Days = 0
        is7DaysDiscountSelected = false
    }

    private fun add30Discount(discount: Int){
        is30DaysDiscountSelected = true
        fragment_add_ad_price_chip_30_days.visibility = View.VISIBLE
        fragment_add_ad_price_chip_30_days.text =
            ("Скидка $discount% на 30 дней")
        discountOn30Days = discount
        onDiscountAdd()
    }

    private fun remove30Discount(){
        fragment_add_ad_price_chip_30_days.visibility = View.GONE
        discountOn30Days = 0
        is30DaysDiscountSelected = false
    }

    private fun onDiscountAdd(){
        if (is7DaysDiscountSelected || is30DaysDiscountSelected)
            fragment_add_ad_price_discounts_layout.visibility = View.VISIBLE
        else
            fragment_add_ad_price_discounts_layout.visibility = View.GONE

        if (is7DaysDiscountSelected && is30DaysDiscountSelected)
            fragment_add_ad_price_add_discount_btn.visibility = View.GONE
        else
            fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
    }

    private fun setToolbar(){
        fragment_add_ad_price_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_price_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdPriceAndDiscounts()
            findNavController().navigateUp()
        }

        fragment_add_ad_price_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }

    override fun onDestroy() {
        sessionManager.clearAddAdPriceAndDiscounts()
        super.onDestroy()
    }
}
















