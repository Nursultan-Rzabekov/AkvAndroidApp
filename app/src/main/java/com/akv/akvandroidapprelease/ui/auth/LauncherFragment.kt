package com.akv.akvandroidapprelease.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akv.akvandroidapprelease.R


class LauncherFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sign_up_nickname, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        nickname_next_btn.setOnClickListener {
//            navRegistration()
//        }
//
//        sign_in_tv.setOnClickListener {
//            navLogin()
//        }

        Log.d(TAG, "LauncherFragment: ${viewModel}")
    }

//    private fun navLogin(){
//        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
//    }
//
//    private fun navRegistration(){
//        val username = sign_nickname_et.text.toString()
//
//        if(username.trim().equals("")) sign_nickname_l_et.error = getString(R.string.invalid)
//        else{
//            sign_nickname_l_et.isErrorEnabled = false
//            val bundle = bundleOf("user_name" to username)
//            findNavController().navigate(R.id.action_launcherFragment_to_register_up_Fragment,bundle)
//        }
//    }
}





















