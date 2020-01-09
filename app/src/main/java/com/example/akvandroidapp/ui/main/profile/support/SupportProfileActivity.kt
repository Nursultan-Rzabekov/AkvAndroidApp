package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
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
        val fragment: Fragment = SupportProfileReviewFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navigateToSecond, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCallUsClicked() {
    }

    override fun onCancelClicked() {
        modalBottomSheet.dismiss()
    }
}
