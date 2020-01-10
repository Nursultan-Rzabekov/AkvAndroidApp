package com.example.akvandroidapp.ui.main.messages


import android.os.Bundle
import android.view.*
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.messages.adapter.MyPagerChatAdapter
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

















