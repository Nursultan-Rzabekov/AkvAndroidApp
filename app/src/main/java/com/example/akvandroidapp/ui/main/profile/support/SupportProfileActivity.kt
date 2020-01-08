package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_support_main.*


class SupportProfileActivity : BaseActivity(), ModalBottomSheet.BottomSheetDialogInteraction
{

    private val modalBottomSheet: ModalBottomSheet = ModalBottomSheet(this)

    override fun displayProgressBar(bool: Boolean) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.support_main_layout)
        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPager)

        main_back_img_btn.setOnClickListener {
            finish()
        }

        fragment_support_main_error_btn.setOnClickListener {
            showDialog()
        }
    }

    override fun expandAppBar() {
    }

    private fun showDialog(){
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
    }

    override fun onTextUsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallUsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCancelClicked() {
        modalBottomSheet.dismiss()
    }
}
