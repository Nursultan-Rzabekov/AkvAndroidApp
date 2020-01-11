package com.example.akvandroidapp.ui.main.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.messages.adapter.ChatRecyclerAdapter
import com.example.akvandroidapp.ui.main.messages.models.Message
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_support_main.*


class MessagesDetailActivity : BaseActivity(), ModalBottomSheetChat.BottomSheetDialogChatInteraction,
    SwipeRefreshLayout.OnRefreshListener
{

    private val mUserId: String = "123"

    private lateinit var chatAdapter: ChatRecyclerAdapter
    private val modalBottomSheet: ModalBottomSheetChat = ModalBottomSheetChat(this)

    override fun displayProgressBar(bool: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_layout)

        swipe_messages.setOnRefreshListener(this)

        initRecyclerView()
        mookDate()

        activity_dialog_attach_btn.setOnClickListener {
            showDialog()
        }

        main_back_img_btn.setOnClickListener {
            finish()
        }

        activity_dialog_send_btn.setOnClickListener {
            sendMessage()
        }
    }

    override fun expandAppBar() {
    }

    private fun initRecyclerView(){
        activity_dialog_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MessagesDetailActivity).apply { stackFromEnd = true }
            chatAdapter = ChatRecyclerAdapter(mUserId)
            adapter = chatAdapter
        }
    }

    private fun mookDate(){
        chatAdapter.addMessage(Message("124", "asdsd"))
        chatAdapter.addMessage(Message("123", "asdsd"))
        chatAdapter.addMessage(Message("123", "asdsd"))
        chatAdapter.addMessage(Message("124", "asdsd"))
        chatAdapter.addMessage(Message("123", "asdsd"))
    }

    private fun sendMessage(){
        val message = activity_dialog_message_et.text.toString()
        if (message.trim() != ""){
            chatAdapter.addMessage(
                Message(mUserId, message)
            )
        }
        activity_dialog_message_et.setText("")
    }

    private fun showDialog(){
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetChat.TAG)
    }

    override fun onCameraClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPhotoClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDocumentClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelClicked() {
        modalBottomSheet.dismiss()
    }

    override fun onRefresh() {
        swipe_messages.isRefreshing = false
    }
}
