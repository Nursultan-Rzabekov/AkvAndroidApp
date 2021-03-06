package com.akv.akvandroidapprelease.ui.main.search.zhilye


import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.entity.ZhilyeDetailProperties
import com.akv.akvandroidapprelease.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_rules_of_house.*
import kotlinx.android.synthetic.main.fragment_rules_of_house_layout.*


class ZhilyeRulesOfHouseActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_rules_of_house_layout)

        header_rules_of_house_toolbar.navigationIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_back)
        setSupportActionBar(header_rules_of_house_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        header_rules_of_house_toolbar.setNavigationOnClickListener{
            finish()
        }

        val passedIntent = intent.extras
        val bundle = passedIntent?.getBundle("houseRules")
        val rules = bundle?.getParcelableArrayList<ZhilyeDetailProperties>("houseRules")
        Log.e("ZhilyeRulesOfHouse", "$rules")
        if (bundle != null && rules != null)
            setRules(rules)
        else
            setRules(arrayListOf())
    }

    private fun setRules(rules: List<ZhilyeDetailProperties>){
        var rulesText = ""
        if (rules.isEmpty())
            rulesText = "Не указано"
        else
            for(i in rules.indices)
                rulesText += "${i+1}. ${rules[i].name}\n"
        rules_house_tv.text = rulesText
    }

    override fun expandAppBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun displayProgressBar(bool: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}