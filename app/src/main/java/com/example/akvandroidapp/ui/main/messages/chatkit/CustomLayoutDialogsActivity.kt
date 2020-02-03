package com.example.akvandroidapp.ui.main.messages.chatkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.akvandroidapp.R
import com.stfalcon.chatkit.dialogs.DialogsList
import com.stfalcon.chatkit.dialogs.DialogsListAdapter

class CustomLayoutDialogsActivity : DemoDialogsActivity() {
    private var dialogsList: DialogsList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_layout_dialogs)
        dialogsList = findViewById(R.id.dialogsList)
        initAdapter()
    }

    private fun initAdapter() {
        super.dialogsAdapter = DialogsListAdapter(
            R.layout.item_custom_dialog,
            super.imageLoader
        )
        super.dialogsAdapter?.setItems(DialogsFixtures.getDialogs())
        super.dialogsAdapter?.setOnDialogClickListener(this)
        super.dialogsAdapter?.setOnDialogLongClickListener(this)
        dialogsList!!.setAdapter(super.dialogsAdapter)
    }

    override fun displayProgressBar(bool: Boolean) {}
    override fun expandAppBar() {}

    override fun onDialogClick(dialog: UserDialogMessages?) {

    }

    override fun onDialogLongClick(dialog: UserDialogMessages?) {

    }

    companion object {
        fun open(context: Context) {
            context.startActivity(
                Intent(
                    context,
                    CustomLayoutDialogsActivity::class.java
                )
            )
        }
    }
}