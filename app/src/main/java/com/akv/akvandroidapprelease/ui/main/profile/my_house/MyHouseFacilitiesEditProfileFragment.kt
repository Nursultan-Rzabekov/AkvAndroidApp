package com.akv.akvandroidapprelease.ui.main.profile.my_house


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.main.profile.add_ad.AddAdCheckboxAdapter
import kotlinx.android.synthetic.main.fragment_add_ad_check.*
import javax.inject.Inject


class MyHouseFacilitiesEditProfileFragment : BaseMyHouseFragment(),
        AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction{

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
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)

        initialState()
        setToolbar()
        initRecyclerView()
        setObservers()
        setSpanable()

        Log.d(TAG, "SearchFragment: ${viewModel}")

        fragment_add_ad_check_next_btn.visibility = View.GONE

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
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, Observer{
            checkboxAdapter.addItems(staticFacilityList, it.facilitiesList!!, isChecked = true, isStatic = false)
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
        val items = mutableListOf<String>()
        for(item in checkboxAdapter.getList()) items.add(item.title)
        if (!items.contains(facility.capitalize()) &&
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

    private fun clearAllFacilities() {
        checkboxAdapter.uncheckAll()
    }

    private fun saveFacilities(){
        val facilities = mutableListOf<String>()
        for (item in checkboxAdapter.getList())
            if (item.isCheked)
                facilities.add(item.title)

        sessionManager.setHouseUpdateFacilityItem(facilities, true)
    }

    private fun setToolbar(){
        fragment_add_ad_check_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_check_toolbar.setNavigationOnClickListener{
            saveFacilities()
            findNavController().navigateUp()
        }

        fragment_add_ad_check_cancel.visibility = View.INVISIBLE
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        checkboxAdapter.isCheckItem(position, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        checkboxAdapter.removeItem(position)
    }

    override fun onDestroy() {
        saveFacilities()
        super.onDestroy()
    }
}