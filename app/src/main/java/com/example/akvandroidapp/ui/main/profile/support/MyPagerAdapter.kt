package com.example.akvandroidapp.ui.main.profile.support

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                GuestProfileFragment()
            }
            else -> {
                return HostProfileFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
                0 -> "Гостю"
            else -> {
                return "Хозяйну"
            }
        }
    }
}