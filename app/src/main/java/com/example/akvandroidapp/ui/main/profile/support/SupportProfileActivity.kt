package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import kotlinx.android.synthetic.main.fragment_support_main.*


class SupportProfileActivity : BaseActivity()
{
    override fun displayProgressBar(bool: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.fragment_support_main)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        support_error.setOnClickListener {
            navigateSettingsFragment()
        }

    }

    override fun expandAppBar() {
    }

    private fun navigateSettingsFragment(){
        findNavController(R.navigation.nav_create_profile).navigate(R.id.action_supportProfileActivity_to_profileSupportProfileReviewFragment)
    }


}
