package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.*
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.Constants
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_address.*
import kotlinx.android.synthetic.main.fragment_add_ad_check.*
import java.util.*
import javax.inject.Inject


class ProfileAddCheckFragment : BaseProfileFragment(), AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction{

    private lateinit var checkboxAdapter: AddAdCheckboxAdapter
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

        setSpanable()
        setAllStaticChechboxes()
        initialState()
        initRecyclerView()

        fragment_add_ad_check_next_btn.setOnClickListener {
            navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            sessionManager.addAdInfo.value?._addAdFacilityList?.clear()
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

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileAddCheckFragment_to_profileAddNearFragment)
    }

    private fun setSpanable(){
        fragment_add_ad_check_drop_all.setText(fragment_add_ad_check_drop_all.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_check_drop_all.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_check_drop_all.text.toString().length, 0)
    }

    private fun assignCheckbox(checkBox: MaterialCheckBox){
        checkBox.setOnCheckedChangeListener { btn, b ->
            sessionManager.setAddAdFacilityListItem(checkBox.text.toString().trim(), b)
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

    private fun initialState(){
        fragment_add_ad_check_add_chkbox_layout.visibility = View.GONE
        fragment_add_ad_check_add_chkbox.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        fragment_add_ad_check_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ProfileAddCheckFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(this@ProfileAddCheckFragment, this@ProfileAddCheckFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun addNewFacility(facility: String) {
        if (!staticFacilityList.contains(facility.capitalize()) &&
            !checkboxAdapter.getList().contains(facility.capitalize()) &&
            facility != "")
            checkboxAdapter.addItem(facility)
        fragment_add_ad_check_add_chkbox_et.setText("")
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

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        sessionManager.setAddAdFacilityListItem(item, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        sessionManager.setAddAdFacilityListItem(item, false)
        checkboxAdapter.removeItem(position)
    }
}

















