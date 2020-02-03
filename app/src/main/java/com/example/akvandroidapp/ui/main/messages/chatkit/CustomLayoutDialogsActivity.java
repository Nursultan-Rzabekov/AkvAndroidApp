package com.example.akvandroidapp.ui.main.messages.chatkit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.akvandroidapp.R;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

public class CustomLayoutDialogsActivity extends DemoDialogsActivity {

    public static void open(Context context) {
        context.startActivity(new Intent(context, CustomLayoutDialogsActivity.class));
    }

    private DialogsList dialogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_layout_dialogs);

        dialogsList =  findViewById(R.id.dialogsList);
        initAdapter();
    }

    private void initAdapter() {
        super.dialogsAdapter = new DialogsListAdapter<>(R.layout.item_custom_dialog, super.imageLoader);
        super.dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        super.dialogsAdapter.setOnDialogClickListener(this);
        super.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(super.dialogsAdapter);
    }

    @Override
    public void displayProgressBar(boolean bool) {

    }

    @Override
    public void expandAppBar() {

    }

    @Override
    public void onDialogClick(UserDialogMessages dialog) {
        CustomLayoutMessagesActivity.open(this);
    }

    @Override
    public void onDialogLongClick(UserDialogMessages dialog) {

    }
}
