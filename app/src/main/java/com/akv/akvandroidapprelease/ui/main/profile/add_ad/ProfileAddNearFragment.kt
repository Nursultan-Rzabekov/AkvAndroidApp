package com.akv.akvandroidapprelease.ui.main.profile.add_ad


import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.session.SessionManager
import kotlinx.android.synthetic.main.fragment_add_ad_near.*
import javax.inject.Inject


class ProfileAddNearFragment : BaseAddHouseFragment(), AddAdCheckboxAdapter.CheckboxCloseInteraction, AddAdCheckboxAdapter.CheckboxCheckInteraction{

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
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            sessionManager.clearAddAdNearList()
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_near, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setSpanable()
        initRecyclerView()
        setObservers()
        initialState()

        fragment_add_ad_near_next_btn.setOnClickListener {
            sessionManager.clearAddAdNearList()
            saveNears()
            Log.e("Sesssion_test_near", "asdasd")
            navNextFragment()
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
            checkboxAdapter.addItems(staticNearList, it._addAdNearByList, isChecked = true, isStatic = false)
        })
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddNearFragment_to_profileAddDatesFragment)
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
        val items = mutableListOf<String>()
        for(item in checkboxAdapter.getList()) items.add(item.title)
        if (!items.contains(near.capitalize()) &&
            near != "")
            checkboxAdapter.addItem(near)
        fragment_add_ad_near_add_chkbox_et.setText("")
    }

    private fun clearAllNear() {
        checkboxAdapter.uncheckAll()
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        checkboxAdapter.isCheckItem(position, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        checkboxAdapter.removeItem(position)
    }

    private fun saveNears(){
        val nears = mutableListOf<String>()
        for (item in checkboxAdapter.getList())
            if (item.isCheked)
                nears.add(item.title)

        sessionManager.setAddAdNearByListItem(nears, true)
    }

    private fun setToolbar(){
        fragment_add_ad_near_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_near_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdNearList()
            findNavController().navigateUp()
        }

        fragment_add_ad_near_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }

    override fun onDestroy() {
        sessionManager.clearAddAdNearList()
        super.onDestroy()
    }
}

















