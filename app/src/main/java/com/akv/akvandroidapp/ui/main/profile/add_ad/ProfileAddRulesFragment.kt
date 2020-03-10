package com.akv.akvandroidapp.ui.main.profile.add_ad


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
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.session.SessionManager
import kotlinx.android.synthetic.main.fragment_add_ad_rules.*
import javax.inject.Inject


class ProfileAddRulesFragment : BaseAddHouseFragment(), AddAdCheckboxAdapter.CheckboxCheckInteraction, AddAdCheckboxAdapter.CheckboxCloseInteraction{

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
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            sessionManager.clearAddAdRulesList()
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_rules, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setSpanable()
        initRecyclerView()
        setObservers()
        initialState()

        fragment_add_ad_rules_next_btn.setOnClickListener {
            sessionManager.clearAddAdRulesList()
            saveRules()
            Log.e("Sesssion_test_rule", "asdasd")
            navNextFragment()
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

    private fun setObservers(){
        sessionManager.addAdInfo.observe(viewLifecycleOwner, Observer{
            checkboxAdapter.addItems(staticRulesList, it._addAdRulesList, isChecked = true, isStatic = false)
        })
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

        sessionManager.setAddAdRulesListItem(rules, true)
    }

    private fun setToolbar(){
        fragment_add_ad_rules_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_rules_toolbar.setNavigationOnClickListener{
            sessionManager.clearAddAdRulesList()
            findNavController().navigateUp()
        }

        fragment_add_ad_rules_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }

    override fun onDestroy() {
        sessionManager.clearAddAdRulesList()
        super.onDestroy()
    }
}

















