package com.akv.akvandroidapprelease.ui.main.messages


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.ui.main.messages.adapter.MyPagerChatAdapter
import kotlinx.android.synthetic.main.fragment_chat_main.*


class MessagesFragment : BaseMessagesFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_main_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter =
            MyPagerChatAdapter(
                childFragmentManager
            )
        view_chat_pager.adapter = fragmentAdapter
        tabs_chat.setupWithViewPager(view_chat_pager)
    }


}

















