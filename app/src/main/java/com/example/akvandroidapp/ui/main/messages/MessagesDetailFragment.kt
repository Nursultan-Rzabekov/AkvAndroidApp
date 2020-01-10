package com.example.akvandroidapp.ui.main.messages


import android.os.Bundle
import android.view.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.messages.adapter.MyPagerChatAdapter
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.fragment_chat_main.*
import kotlinx.android.synthetic.main.fragment_chats.*


class MessagesDetailFragment : BaseMessagesFragment(), SwipeRefreshLayout.OnRefreshListener{


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_dialog_layout, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_messages.setOnRefreshListener(this)

    }


    override fun onRefresh() {
        swipe_messages.isRefreshing = false
    }

}

















