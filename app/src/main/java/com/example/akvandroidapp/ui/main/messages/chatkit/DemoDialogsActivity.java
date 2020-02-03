package com.example.akvandroidapp.ui.main.messages.chatkit;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.example.akvandroidapp.ui.BaseActivity;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;


public abstract class DemoDialogsActivity extends BaseActivity
        implements DialogsListAdapter.OnDialogClickListener<UserDialogMessages>,
        DialogsListAdapter.OnDialogLongClickListener<UserDialogMessages> {

    protected ImageLoader imageLoader;
    protected DialogsListAdapter<UserDialogMessages> dialogsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = (imageView, url, payload) -> Picasso.with(DemoDialogsActivity.this).load(url).into(imageView);
    }
}
