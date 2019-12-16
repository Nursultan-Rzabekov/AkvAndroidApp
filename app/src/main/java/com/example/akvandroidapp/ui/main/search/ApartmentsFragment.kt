package com.example.akvandroidapp.ui.main.search


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView

import com.example.akvandroidapp.R
import com.example.akvandroidapp.util.PasswordChecker
import kotlinx.android.synthetic.main.header_searcher_base_layout.*
import kotlinx.android.synthetic.main.search_part_layout.*
import kotlinx.android.synthetic.main.sign_up_pass.*


class ApartmentsFragment : BaseSearchFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.search_part_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragement_explore_layout_id.visibility = View.GONE
        fragment_appartments_layout_id.visibility = View.VISIBLE
        Log.d(TAG, "SearchFragment: ${viewModel}")


        by_map_chip.setOnClickListener {
            navMapActivity()
        }

        by_date_chip.setOnClickListener {
            showDateDialog()
        }

    }

    private fun navMapActivity(){
        findNavController().navigate(R.id.action_apartmentsFragment_to_mapActivity)
    }

    private fun showDateDialog(){

        activity?.let {
            val dialog = MaterialDialog(it).noAutoDismiss()
                .customView(R.layout.dialog_guests)

            val view = dialog.getCustomView()

            view.findViewById<ImageButton>(R.id.dialog_date_cancel).setOnClickListener {
                Log.d(TAG, "FilterDialog: cancelling filter.")
                dialog.dismiss()
            }

            dialog.show()
        }
    }

}