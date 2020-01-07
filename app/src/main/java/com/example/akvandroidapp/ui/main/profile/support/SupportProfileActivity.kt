package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import android.os.PersistableBundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_support_main.*


class SupportProfileActivity : BaseActivity()
{
    override fun displayProgressBar(bool: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_main_layout)
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        main_back_img_btn.setOnClickListener {
            finish()
        }
    }

    override fun expandAppBar() {
    }
}
