package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.messages.adapter.MyPagerChatAdapter
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_chat_main.*
import kotlinx.android.synthetic.main.fragment_support_main.*
import kotlinx.android.synthetic.main.support_main_layout.*


class SupportProfileFragment : BaseSupportFragment(), ModalBottomSheet.BottomSheetDialogInteraction
{

    private val modalBottomSheet: ModalBottomSheet = ModalBottomSheet(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.support_main_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAdapter =
            MyPagerAdapter(
                childFragmentManager
            )
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        fragment_support_main_error_btn.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog(){
        modalBottomSheet.show(childFragmentManager, ModalBottomSheet.TAG)
    }

    override fun onTextUsClicked() {
        findNavController().navigate(R.id.action_supportProfileFragment_to_profileSupportProfileReviewFragment)
    }

    override fun onCallUsClicked() {
    }

    override fun onCancelClicked() {
        modalBottomSheet.dismiss()
    }
}
