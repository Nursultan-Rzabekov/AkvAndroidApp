package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_type_of_appartment.*


class ProfileAddTypeFragment : BaseProfileFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_type_of_appartment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_add_ad_type_of_appartment_next_btn.setOnClickListener {
            navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navNextFragment(){
        findNavController().navigate(R.id.action_profileAddTypeFragment_to_profileAddQuestsFragment)
    }
}

















