package com.akv.akvandroidapprelease.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapprelease.R
import kotlinx.android.synthetic.main.reset_code.*


class ResetCodeFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.reset_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "ResetCodeFragment: ${viewModel}")

        materialButton.setOnClickListener {
            navNextNavigationPage()
        }

        reset_code_back_tv.setOnClickListener {
            findNavController().navigate(R.id.action_resetCodeFragment_to_LoginGmailFragment)
        }

    }

    private fun navNextNavigationPage(){
        val code = reset_code_et.text.toString()

        if(code.trim().equals("")) reset_code_l_et.error = getString(R.string.invalid)
        else{
            reset_code_l_et.isErrorEnabled = false
            val bundle = bundleOf("code" to code)
            findNavController().navigate(R.id.action_resetCodeFragment_to_NewPassFragment,bundle)
        }
    }
}
