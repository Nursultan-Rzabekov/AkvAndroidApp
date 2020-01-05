package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_price.*
import javax.inject.Inject


class ProfileAddPriceFragment : BaseProfileFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    private var isOneOptionLeft = false
    private var is7DaysDiscountSelected = false
    private var is30DaysDiscountSelected = false

    private var discountOn7Days = 0
    private var discountOn30Days = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialState()

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        fragment_add_ad_price_next_btn.setOnClickListener {
            savePrice()
            navNextFragment()
        }

        fragment_add_ad_price_add_discount_btn.setOnClickListener {
            if (fragment_add_ad_price_discounts_add_layout.visibility == View.GONE) {
                fragment_add_ad_price_discounts_add_layout.visibility = View.VISIBLE
                fragment_add_ad_price_add_discount_btn.visibility = View.GONE

                if (isOneOptionLeft) {
                    fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.VISIBLE
                    fragment_add_ad_price_discounts_add_choose_days.visibility = View.GONE
                } else {
                    fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.GONE
                    fragment_add_ad_price_discounts_add_choose_days.visibility = View.VISIBLE
                }
            }
        }

        fragment_add_ad_price_remove_btn.setOnClickListener {
            fragment_add_ad_price_discounts_add_layout.visibility = View.GONE
            fragment_add_ad_price_seekbar.progress = 0
            fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
        }

        fragment_add_ad_price_add_btn.setOnClickListener {
            fragment_add_ad_price_discounts_add_layout.visibility = View.GONE

            if (fragment_add_ad_price_seekbar.progress > 0) {
                if (!isOneOptionLeft) {
                    when (fragment_add_ad_price_discounts_radio_group.checkedRadioButtonId) {
                        fragment_add_ad_price_discounts_add_7_days_rbtn.id -> {
                            is7DaysDiscountSelected = true
                            fragment_add_ad_price_chip_7_days.visibility = View.VISIBLE
                            fragment_add_ad_price_chip_7_days.text =
                                ("Скидка ${fragment_add_ad_price_seekbar.progress}% на 7 дней")
                            discountOn7Days = fragment_add_ad_price_seekbar.progress
                        }
                        fragment_add_ad_price_discounts_add_30_days_rbtn.id -> {
                            is30DaysDiscountSelected = true
                            fragment_add_ad_price_chip_30_days.visibility = View.VISIBLE
                            fragment_add_ad_price_chip_30_days.text =
                                ("Скидка ${fragment_add_ad_price_seekbar.progress}% на 30 дней")
                            discountOn30Days = fragment_add_ad_price_seekbar.progress
                        }
                    }
                } else {

                    if (is7DaysDiscountSelected) {
                        fragment_add_ad_price_chip_30_days.visibility = View.VISIBLE
                        fragment_add_ad_price_chip_30_days.text =
                            ("Скидка ${fragment_add_ad_price_seekbar.progress}% на 30 дней")
                        discountOn30Days = fragment_add_ad_price_seekbar.progress
                    }
                    if (is30DaysDiscountSelected) {
                        fragment_add_ad_price_chip_7_days.visibility = View.VISIBLE
                        fragment_add_ad_price_chip_7_days.text =
                            ("Скидка ${fragment_add_ad_price_seekbar.progress}% на 7 дней")
                        discountOn7Days = fragment_add_ad_price_seekbar.progress
                    }

                    is30DaysDiscountSelected = true
                    is7DaysDiscountSelected = true
                }

                if (!isOneOptionLeft) isOneOptionLeft = true
            }

            if (is7DaysDiscountSelected || is30DaysDiscountSelected)
                fragment_add_ad_price_discounts_layout.visibility = View.VISIBLE
            else
                fragment_add_ad_price_discounts_layout.visibility = View.GONE

            if (is7DaysDiscountSelected && is30DaysDiscountSelected)
                fragment_add_ad_price_add_discount_btn.visibility = View.GONE
            else
                fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
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
            fragment_add_ad_price_chip_7_days.visibility = View.GONE

            is7DaysDiscountSelected = false
            if (!is30DaysDiscountSelected) {
                isOneOptionLeft = false
                fragment_add_ad_price_discounts_layout.visibility = View.GONE
            }else isOneOptionLeft = true

            onChipClose()
        }

        fragment_add_ad_price_chip_30_days.setOnCloseIconClickListener {
            fragment_add_ad_price_chip_30_days.visibility = View.GONE

            is30DaysDiscountSelected = false
            if (!is7DaysDiscountSelected){
                isOneOptionLeft = false
                fragment_add_ad_price_discounts_layout.visibility = View.GONE
            }else isOneOptionLeft = true

            onChipClose()
        }

        fragment_add_ad_price_discounts_add_7_days_rbtn.setOnClickListener {
            fragment_add_ad_price_one_option_tv.text = ("Кол-во дней: 30 дней")
        }

        fragment_add_ad_price_discounts_add_30_days_rbtn.setOnClickListener {
            fragment_add_ad_price_one_option_tv.text = ("Кол-во дней: 7 дней")
        }
    }

    private fun initialState(){
        fragment_add_ad_price_discounts_layout.visibility = View.GONE
        fragment_add_ad_price_discounts_add_layout.visibility = View.GONE
        fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
    }

    private fun onChipClose(){
        if (fragment_add_ad_price_discounts_add_layout.visibility == View.GONE){
            fragment_add_ad_price_add_discount_btn.visibility = View.VISIBLE
        } else
            fragment_add_ad_price_add_discount_btn.visibility = View.GONE

        if (isOneOptionLeft){
            fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.VISIBLE
            fragment_add_ad_price_discounts_add_choose_days.visibility = View.GONE
        } else {
            fragment_add_ad_price_discounts_add_choose_days_shrink.visibility = View.GONE
            fragment_add_ad_price_discounts_add_choose_days.visibility = View.VISIBLE
        }
    }

    private fun savePrice(){
        sessionManager.setAddAdPriceAndDiscounts(
            fragment_add_ad_price_et.text.toString().toInt(),
            discountOn7Days,
            discountOn30Days)
    }


    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddPriceFragment_to_profileAddPostFragment)
    }
}
















