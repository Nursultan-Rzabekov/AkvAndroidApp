package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_near.*
import javax.inject.Inject


class ProfileAddNearFragment : BaseAddHouseFragment(), AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction{

    private val nears = mutableListOf<String>()
    private lateinit var checkboxAdapter: AddAdCheckboxAdapter
    private val staticNearList = mutableListOf(
        "Больница",
        "Парк",
        "Автовокзал",
        "Аптека",
        "Кинотеатр",
        "Детский сад",
        "ЦОН",
        "Школа",
        "Акимат")

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_near, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpanable()
        initRecyclerView()
        setAllStaticChechboxes()
        setObservers()
        initialState()

        fragment_add_ad_near_next_btn.setOnClickListener {
            sessionManager.clearAddAdNearList()
            saveNears()
            Log.e("Sesssion_test_near", "$nears")
            nears.clear()
            navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            sessionManager.clearAddAdNearList()
            findNavController().navigateUp()
        }

        fragment_add_ad_near_add_chkbox.setOnClickListener {
            fragment_add_ad_near_add_chkbox.visibility = View.GONE
            fragment_add_ad_near_add_chkbox_layout.visibility = View.VISIBLE
        }

        fragment_add_ad_near_remove_chkbox_btn.setOnClickListener {
            initialState()
        }

        fragment_add_ad_near_add_chkbox_btn.setOnClickListener {
            addNewNear(fragment_add_ad_near_add_chkbox_et.text.toString().trim())
            initialState()
        }

        fragment_add_ad_near_drop_all.setOnClickListener {
            clearAllNear()
        }
    }

    private fun setSpanable(){
        fragment_add_ad_near_drop_all.setText(fragment_add_ad_near_drop_all.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_near_drop_all.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_near_drop_all.text.toString().length, 0)
    }

    private fun setObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer{
            val initialItems = mutableListOf<String>()
            for(item in it._addAdNearByList) {
                initialItems.add(item)
            }
            checkboxAdapter.addAllItems(initialItems, isChecked = true, isStatic = false)
        })
    }

    private fun setAllStaticChechboxes(){
        checkboxAdapter.addAllItems(staticNearList, isStatic = true)
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddNearFragment_to_profileAddRequirementFragment)
    }

    private fun initialState(){
        fragment_add_ad_near_add_chkbox_layout.visibility = View.GONE
        fragment_add_ad_near_add_chkbox.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        fragment_add_ad_near_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ProfileAddNearFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(this@ProfileAddNearFragment, this@ProfileAddNearFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun addNewNear(near: String) {
        if (!checkboxAdapter.getList().contains(near.capitalize()) &&
            near != "")
            checkboxAdapter.addItem(near)
        fragment_add_ad_near_add_chkbox_et.setText("")
    }

    private fun clearAllNear() {
        checkboxAdapter.uncheckAll()
        nears.clear()
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        addOrRemoveNear(item, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        addOrRemoveNear(item, false)
        checkboxAdapter.removeItem(position)
    }

    private fun addOrRemoveNear(item: String, checked: Boolean) {
        if (checked)
            nears.add(item)
        else
            nears.remove(item)
    }

    private fun saveNears(){
        for (item in nears)
            sessionManager.setAddAdNearByListItem(item, true)
    }
}

















