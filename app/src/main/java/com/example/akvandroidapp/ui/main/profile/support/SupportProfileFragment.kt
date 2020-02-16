package com.example.akvandroidapp.ui.main.profile.support

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.messages.adapter.MyPagerChatAdapter
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_chat_main.*
import kotlinx.android.synthetic.main.fragment_support_main.*
import kotlinx.android.synthetic.main.support_main_layout.*
import java.util.jar.Manifest


class SupportProfileFragment : BaseSupportFragment(), ModalBottomSheet.BottomSheetDialogInteraction
{

    private val MY_PERMISSIONS_REQUEST_CALL_PHONE = 554
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

        setToolbar()

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
        callAkvService()
    }

    override fun onCancelClicked() {
        modalBottomSheet.dismiss()
    }

    private fun setToolbar(){
        header_support_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_support_toolbar.setNavigationOnClickListener{
            activity?.finish()
        }
    }

    private fun callAkvService(){
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:${Constants.AKV_PHONE}")
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE),
                MY_PERMISSIONS_REQUEST_CALL_PHONE)
        else {
            try {
                startActivity(callIntent)
            }catch (e: SecurityException){
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            MY_PERMISSIONS_REQUEST_CALL_PHONE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callAkvService()
                }
                else{
                    Toast.makeText(requireContext(), "Oops", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
