package com.example.akvandroidapp.ui.main.messages.chatkit

import android.os.Bundle
import android.widget.ImageView
import com.example.akvandroidapp.ui.BaseActivity
import com.squareup.picasso.Picasso
import com.stfalcon.chatkit.commons.ImageLoader
import com.stfalcon.chatkit.dialogs.DialogsListAdapter
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogClickListener
import com.stfalcon.chatkit.dialogs.DialogsListAdapter.OnDialogLongClickListener

abstract class DemoDialogsActivity : BaseActivity(),
    OnDialogClickListener<UserDialogMessages?>, OnDialogLongClickListener<UserDialogMessages?> {
    protected var imageLoader: ImageLoader? = null
    protected var dialogsAdapter: DialogsListAdapter<UserDialogMessages>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageLoader =
            ImageLoader { imageView: ImageView?, url: String?, payload: Any? ->
                Picasso.with(this@DemoDialogsActivity).load(url).into(imageView)
            }
    }
}