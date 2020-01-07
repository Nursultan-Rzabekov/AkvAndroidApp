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
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_rules.*
import javax.inject.Inject


class ProfileAddRulesFragment : BaseProfileFragment(), AddAdCheckboxAdapter.CheckboxCheckInteraction, AddAdCheckboxAdapter.CheckboxCloseInteraction{

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
        setSpanable()
        setAllStaticChechboxes()
        initialState()
        initRecyclerView()

        fragment_add_ad_rules_next_btn.setOnClickListener {
            navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            sessionManager.clearAddAdRulesList()
            findNavController().navigateUp()
        }
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

    private fun navNextFragment(){
        findNavController().navigate(R.id.profileAddRulesFragment_to_profileAddPriceFragment)
    }

    private fun setSpanable(){
        fragment_add_ad_rules_drop_all.setText(fragment_add_ad_rules_drop_all.text.toString(), TextView.BufferType.SPANNABLE)
        val span4 = fragment_add_ad_rules_drop_all.text as Spannable
        span4.setSpan(UnderlineSpan(), 0, fragment_add_ad_rules_drop_all.text.toString().length, 0)
    }

    private fun assignCheckbox(checkBox: MaterialCheckBox){
        checkBox.setOnCheckedChangeListener { btn, b ->
            sessionManager.setAddAdRulesListItem(checkBox.text.toString().trim(), b)
        }
    }

    private fun setAllStaticChechboxes(){
        assignCheckbox(fragment_add_ad_rules_chkbox1)
        assignCheckbox(fragment_add_ad_rules_chkbox2)
        assignCheckbox(fragment_add_ad_rules_chkbox3)
        assignCheckbox(fragment_add_ad_rules_chkbox4)
        assignCheckbox(fragment_add_ad_rules_chkbox5)
        assignCheckbox(fragment_add_ad_rules_chkbox6)
    }

    private fun initialState(){
        fragment_add_ad_rules_add_chkbox_layout.visibility = View.GONE
        fragment_add_ad_rules_add_chkbox.visibility = View.VISIBLE
    }

    private fun initRecyclerView(){
        fragment_add_ad_rules_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@ProfileAddRulesFragment.context)
            checkboxAdapter = AddAdCheckboxAdapter(this@ProfileAddRulesFragment, this@ProfileAddRulesFragment)
            adapter = checkboxAdapter
        }
        checkboxAdapter.submitList(mutableListOf())
    }

    private fun addNewRule(rule: String) {
        if (!staticRulesList.contains(rule.capitalize()) &&
            !checkboxAdapter.getList().contains(rule.capitalize()) &&
            rule != "")
            checkboxAdapter.addItem(rule)
        fragment_add_ad_rules_add_chkbox_et.setText("")
    }

    private fun clearAllRules() {
        checkboxAdapter.uncheckAll()
        uncheckStaticFacilities(fragment_add_ad_rules_chkbox1)
        uncheckStaticFacilities(fragment_add_ad_rules_chkbox2)
        uncheckStaticFacilities(fragment_add_ad_rules_chkbox3)
        uncheckStaticFacilities(fragment_add_ad_rules_chkbox4)
        uncheckStaticFacilities(fragment_add_ad_rules_chkbox5)
        uncheckStaticFacilities(fragment_add_ad_rules_chkbox6)
    }

    private fun uncheckStaticFacilities(checkBox: MaterialCheckBox) {
        checkBox.isChecked = false
    }

    override fun onItemChecked(position: Int, item: String, checked: Boolean) {
        sessionManager.setAddAdRulesListItem(item, checked)
    }

    override fun onItemClosed(position: Int, item: String) {
        sessionManager.setAddAdRulesListItem(item, false)
        checkboxAdapter.removeItem(position)
    }
}

















