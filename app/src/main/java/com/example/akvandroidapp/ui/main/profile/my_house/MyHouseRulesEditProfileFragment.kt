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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.AddAdCheckboxAdapter
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_rules.*
import javax.inject.Inject


class MyHouseRulesEditProfileFragment : BaseMyHouseFragment(), AddAdCheckboxAdapter.CheckboxCheckInteraction, AddAdCheckboxAdapter.CheckboxCloseInteraction{

    private lateinit var checkboxAdapter: AddAdCheckboxAdapter
    private val staticRulesList = mutableListOf(
        "Не курить",
        "Никакого алкоголя",
        "Не шуметь",
        "Не сорить",
        "Небольше 2х гостей",
        "Никаких животных"
    )

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setSpanable()
        initRecyclerView()
        setObservers()
        initialState()

        fragment_add_ad_rules_next_btn.visibility = View.GONE

//        main_back_img_btn.setOnClickListener {
//            sessionManager.clearHouseUpdateRules()
//            saveRules()
//            Log.e("Sesssion_test_rule", "sadsd")
//            findNavController().navigateUp()
//        }

        fragment_add_ad_rules_add_chkbox.setOnClickListener {
            fragment_add_ad_rules_add_chkbox.visibility = View.GONE
            fragment_add_ad_rules_add_chkbox_layout.visibility = View.VISIBLE
        }

        fragment_add_ad_rules_remove_chkbox_btn.setOnClickListener {
            initialState()
        }

        fragment_add_ad_rules_add_chkbox_btn.setOnClickListener {
            addNewRule(fragment_add_ad_rules_add_chkbox_et.text.toString().trim())
            initialState()
        }

        fragment_add_ad_rules_drop_all.setOnClickListener {
            clearAllRules()
        }
    }

    private fun setSpanable(){
        fragment_add_ad_rules_drop_all.setText(fragment_add_ad_rules_drop_all.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_rules_drop_all.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_rules_drop_all.text.toString().length, 0)
    }

    private fun setObservers(){
        sessionManager.houseUpdateData.observe(viewLifecycleOwner, Observer{
            checkboxAdapter.addItems(staticRulesList, it.houseRulesList!!, isChecked = true, isStatic = false)
        })
    }

    private fun initialState(){
        fragment_add_ad_rules_add_chkbox_layout.visibility = View.GONE
        fragment_add_ad_rules_add_chkbox.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        fragment_add_ad_rules_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MyHouseRulesEditProfileFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(this@MyHouseRulesEditProfileFragment, this@MyHouseRulesEditProfileFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun addNewRule(rule: String) {
        val items = mutableListOf<String>()
        for(item in checkboxAdapter.getList()) items.add(item.title)
        if (!items.contains(rule.capitalize()) &&
            rule != "")
            checkboxAdapter.addItem(rule)
        fragment_add_ad_rules_add_chkbox_et.setText("")
    }

    private fun clearAllRules() {
        checkboxAdapter.uncheckAll()
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        checkboxAdapter.isCheckItem(position, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        checkboxAdapter.removeItem(position)
    }

    private fun saveRules(){
        val rules = mutableListOf<String>()
        for (item in checkboxAdapter.getList())
            if (item.isCheked)
                rules.add(item.title)

        sessionManager.setHouseUpdateRulesItem(rules, true)
    }
}