package com.akv.akvandroidapp.ui.main.messages.chatkit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akv.akvandroidapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.chat_bottom_sheet_dialog.view.*


class ModalBottomSheetChat(
    private val bottomSheetDialogInteraction: BottomSheetDialogChatInteraction
) : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.chat_bottom_sheet_dialog_cancel_btn.setOnClickListener {
            bottomSheetDialogInteraction.onCancelClicked()
        }

        view.chat_bottom_sheet_dialog_camera_btn.setOnClickListener {
            bottomSheetDialogInteraction.onCameraClicked()
        }

        view.chat_bottom_sheet_dialog_photo_btn.setOnClickListener {
            bottomSheetDialogInteraction.onPhotoClicked()
        }

        view.chat_bottom_sheet_dialog_document_btn.setOnClickListener {
            bottomSheetDialogInteraction.onDocumentClicked()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    interface BottomSheetDialogChatInteraction{
        fun onCameraClicked()
        fun onPhotoClicked()
        fun onDocumentClicked()
        fun onCancelClicked()
    }
}