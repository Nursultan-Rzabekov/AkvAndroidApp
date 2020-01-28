package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.util.Constants
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.activity_add_ad.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_address.*
import kotlinx.android.synthetic.main.fragment_add_ad_check.*
import javax.inject.Inject


class ProfileAddCheckFragment : BaseAddHouseFragment(), AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction{

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
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            sessionManager.clearAddAdFacilityList()
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setSpanable()
        initRecyclerView()
        setObservers()
        initialState()

        fragment_add_ad_check_next_btn.setOnClickListener {
            sessionManager.clearAddAdFacilityList()
            saveFacilities()
            Log.e("Sesssion_test_faci", "asdasd")
            navNextFragment()
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

    private fun setObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer{
            checkboxAdapter.addItems(staticFacilityList, it._addAdFacilityList, isChecked = true, isStatic = false)
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

    private fun initRecyclerView(){
        fragment_add_ad_check_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ProfileAddCheckFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(this@ProfileAddCheckFragment, this@ProfileAddCheckFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun addNewFacility(facility: String) {
        val items = mutableListOf<String>()
        for(item in checkboxAdapter.getList()) items.add(item.title)
        if (!items.contains(facility.capitalize()) &&
            facility != "")
            checkboxAdapter.addItem(facility)
        fragment_add_ad_check_add_chkbox_et.setText("")
    }

    private fun clearAllFacilities() {
        checkboxAdapter.uncheckAll()
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        checkboxAdapter.isCheckItem(position, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        checkboxAdapter.removeItem(position)
    }

    private fun saveFacilities(){
        val facilities = mutableListOf<String>()
        for (item in checkboxAdapter.getList())
            if (item.isCheked)
                facilities.add(item.title)

        sessionManager.setAddAdFacilityListItem(facilities, true)
    }

    private fun setToolbar(){
        fragment_add_ad_check_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_check_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdFacilityList()
            findNavController().navigateUp()
        }

        fragment_add_ad_check_cancel.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onDestroy() {
        sessionManager.clearAddAdFacilityList()
        super.onDestroy()
    }
}

















