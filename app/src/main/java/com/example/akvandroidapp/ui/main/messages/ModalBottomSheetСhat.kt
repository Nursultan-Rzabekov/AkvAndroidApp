package com.example.akvandroidapp.ui.main.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.akvandroidapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.chat_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.support_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.support_bottom_sheet_dialog.view.support_bottom_sheet_dialog_text_us_btn


class ModalBottomSheetСhat(
    private val bottomSheetDialogInteraction: BottomSheetDialogInteraction
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

        view.cancel_sheet.setOnClickListener {
            bottomSheetDialogInteraction.onCancelClicked()
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    interface BottomSheetDialogInteraction{
        fun onTextUsClicked()
        fun onCallUsClicked()
        fun onCancelClicked()
    }
}