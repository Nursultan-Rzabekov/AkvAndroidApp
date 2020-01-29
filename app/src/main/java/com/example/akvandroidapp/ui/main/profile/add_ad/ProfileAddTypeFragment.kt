package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.adapter.RegionAutoCompleteAdapter
import com.example.akvandroidapp.util.Constants
import kotlinx.android.synthetic.main.activity_add_ad.*
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_type_of_appartment.*
import kotlinx.android.synthetic.main.header_zhilye.*
import javax.inject.Inject


class ProfileAddTypeFragment : BaseAddHouseFragment(){

    private val typesOfAppartments = listOf("Квартира", "Дом")

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity_add_ad_toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp()
//        }
        return inflater.inflate(R.layout.fragment_add_ad_type_of_appartment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setDropDown()

        fragment_add_ad_type_of_appartment_next_btn.setOnClickListener {
            val type = fragment_add_ad_type_of_appartment_drop_down_auto_tv.text.toString()

            if (type != "")
                navNextFragment(type)
            else
                Toast.makeText(requireContext(), "Выберите тип жилья", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setDropDown(){
        val typesAdapter = RegionAutoCompleteAdapter(
            requireContext(),
            Constants.typeList)
        fragment_add_ad_type_of_appartment_drop_down_auto_tv.setAdapter(typesAdapter)
    }

    private fun navNextFragment(type: String){
        sessionManager.setAddAdType(type)
        findNavController().navigate(R.id.action_profileAddTypeFragment_to_profileAddQuestsFragment)
    }

    private fun setToolbar(){
        fragment_add_ad_type_of_appartment_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        fragment_add_ad_type_of_appartment_toolbar.setNavigationOnClickListener{
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }

        fragment_add_ad_type_of_appartment_cancel.setOnClickListener {
            activity?.finish()
            sessionManager.clearAddAdAllInfo()
        }
    }
}

















