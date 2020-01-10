package com.example.akvandroidapp.ui.main.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_support_main.*


class MessagesDetailActivity : BaseActivity(), ModalBottomSheetСhat.BottomSheetDialogInteraction,
    SwipeRefreshLayout.OnRefreshListener
{

    private val modalBottomSheet: ModalBottomSheetСhat = ModalBottomSheetСhat(this)

    override fun displayProgressBar(bool: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_layout)

        swipe_messages.setOnRefreshListener(this)

        add_chat.setOnClickListener {
            showDialog()
        }

        main_back_img_btn.setOnClickListener {
            finish()
        }
    }

    override fun expandAppBar() {
    }

    private fun showDialog(){
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetСhat.TAG)
    }

    override fun onTextUsClicked() {
    }


    override fun onCallUsClicked() {
    }

    override fun onCancelClicked() {
        modalBottomSheet.dismiss()
    }

    override fun onRefresh() {
        swipe_messages.isRefreshing = false
    }
}
