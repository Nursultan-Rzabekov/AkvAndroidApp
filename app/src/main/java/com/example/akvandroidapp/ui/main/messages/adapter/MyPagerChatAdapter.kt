package com.example.akvandroidapp.ui.main.messages.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.akvandroidapp.ui.main.messages.ChatMesFragment
import com.example.akvandroidapp.ui.main.messages.RequestFragment

class MyPagerChatAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                ChatMesFragment()
            }
            else -> {
                return RequestFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
                0 -> "Чаты"
            else -> {
                return "Заявки"
            }
        }
    }
}