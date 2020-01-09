package com.example.akvandroidapp.ui.main.profile.my_house


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
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
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.AddAdCheckboxAdapter
import com.example.akvandroidapp.ui.main.profile.add_ad.CheckboxItem
import com.example.akvandroidapp.util.PasswordChecker
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.fragment_add_ad_check.*
import kotlinx.android.synthetic.main.fragment_add_ad_rules.*
import kotlinx.android.synthetic.main.sign_up_pass.*
import javax.inject.Inject


class MyHouseFacilitiesEditProfileFragment : BaseProfileFragment(),
        AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction{

    private lateinit var checkboxAdapter: AddAdCheckboxAdapter
    private var facilities: MutableList<String>? = mutableListOf()
    private val staticFacilityList = mutableListOf(
        "Кондиционер",
        "Фен",
        "Отопление",
        "Утюг",
        "Wifi",
        "Полотенца",
        "Телевизор",
        "Телефон",
        "Аптечка")

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        initRecyclerView()
        initialState()
        setSpanable()
        setObservers()
        setAllStaticChechboxes()
        checkboxAdapter.notifyDataSetChanged()

        Log.d(TAG, "SearchFragment: ${viewModel}")

        fragment_add_ad_check_next_btn.visibility = View.GONE

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        fragment_add_ad_check_add_chkbox.setOnClickListener {
            fragment_add_ad_check_add_chkbox.visibility = View.GONE
            fragment_add_ad_check_add_chkbox_layout.visibility = View.VISIBLE
        }

        fragment_add_ad_check_remove_chkbox_btn.setOnClickListener {
            initialState()
        }

        fragment_add_ad_check_add_chkbox_btn.setOnClickListener {
            addNewFacility(fragment_add_ad_check_add_chkbox_et.text.toString().trim())
            initialState()
        }

        fragment_add_ad_check_drop_all.setOnClickListener {
            clearAllFacilities()
        }
    }

    private fun setObservers(){
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, Observer {
            if (it.facilitiesList != null) {
                val initialItems: MutableList<CheckboxItem> = mutableListOf()
                for (item in it.facilitiesList?.toMutableList()!!) {
                    Log.e("my_house Faciliti List", item)
                    if (item in staticFacilityList)
                        checkStaticCheckbox(item)
                    else
                        initialItems.add(CheckboxItem(item, true))
                }
                checkboxAdapter.submitList(initialItems)
                checkboxAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun setSpanable(){
        fragment_add_ad_check_drop_all.setText(fragment_add_ad_check_drop_all.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_check_drop_all.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_check_drop_all.text.toString().length, 0)
    }

    private fun initialState(){
        fragment_add_ad_check_add_chkbox_layout.visibility = View.GONE
        fragment_add_ad_check_add_chkbox.visibility = View.VISIBLE
    }

    private fun addNewFacility(facility: String) {
        if (!staticFacilityList.contains(facility.capitalize()) &&
            !checkboxAdapter.getList().contains(facility.capitalize()) &&
            facility != "")
            checkboxAdapter.addItem(facility)
        fragment_add_ad_check_add_chkbox_et.setText("")
    }

    private fun initRecyclerView(){
        fragment_add_ad_check_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseFacilitiesEditProfileFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(
                this@MyHouseFacilitiesEditProfileFragment,
                this@MyHouseFacilitiesEditProfileFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun checkStaticCheckbox(item: String){
        when(item) {
            "Кондиционер" -> fragment_add_ad_check_chkbox1.isChecked = true
            "Фен" -> fragment_add_ad_check_chkbox2.isChecked = true
            "Отопление" -> fragment_add_ad_check_chkbox3.isChecked = true
            "Утюг" -> fragment_add_ad_check_chkbox4.isChecked = true
            "Wifi" -> fragment_add_ad_check_chkbox5.isChecked = true
            "Полотенца" -> fragment_add_ad_check_chkbox6.isChecked = true
            "Телевизор" -> fragment_add_ad_check_chkbox7.isChecked = true
            "Телефон" -> fragment_add_ad_check_chkbox8.isChecked = true
            "Аптечка" -> fragment_add_ad_check_chkbox9.isChecked = true
        }
    }

    private fun assignCheckbox(checkBox: MaterialCheckBox){
        checkBox.setOnCheckedChangeListener { btn, b ->
            addOrRemoveItemToLocalFacilities(btn.text.toString().trim(), b)
        }
    }

    private fun setAllStaticChechboxes(){
        assignCheckbox(fragment_add_ad_check_chkbox1)
        assignCheckbox(fragment_add_ad_check_chkbox2)
        assignCheckbox(fragment_add_ad_check_chkbox3)
        assignCheckbox(fragment_add_ad_check_chkbox4)
        assignCheckbox(fragment_add_ad_check_chkbox5)
        assignCheckbox(fragment_add_ad_check_chkbox6)
        assignCheckbox(fragment_add_ad_check_chkbox7)
        assignCheckbox(fragment_add_ad_check_chkbox8)
        assignCheckbox(fragment_add_ad_check_chkbox9)
    }

    private fun clearAllFacilities() {
        checkboxAdapter.uncheckAll()
        uncheckStaticFacilities(fragment_add_ad_check_chkbox1)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox2)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox3)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox4)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox5)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox6)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox7)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox8)
        uncheckStaticFacilities(fragment_add_ad_check_chkbox9)
    }

    private fun uncheckStaticFacilities(checkBox: MaterialCheckBox) {
        checkBox.isChecked = false
    }

    private fun addOrRemoveItemToLocalFacilities(item:String, checked: Boolean){
        if (checked)
            facilities?.add(item)
        else
            facilities?.remove(item)
        Log.e("MY_HOUSE_EDIT_FAC", "$facilities")
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        addOrRemoveItemToLocalFacilities(item, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        addOrRemoveItemToLocalFacilities(item, false)
        checkboxAdapter.removeItem(position)
    }
}