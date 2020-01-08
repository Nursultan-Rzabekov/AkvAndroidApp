package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.akvandroidapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.support_bottom_sheet_dialog.view.*


class ModalBottomSheet(
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
        return inflater.inflate(R.layout.support_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.support_bottom_sheet_dialog_cancel_btn.setOnClickListener {
            bottomSheetDialogInteraction.onCancelClicked()
        }

        view.support_bottom_sheet_dialog_text_us_btn.setOnClickListener {
            bottomSheetDialogInteraction.onTextUsClicked()
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