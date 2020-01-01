package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_type_of_appartment.*


class ProfileAddTypeFragment : BaseProfileFragment(){

    private val typesOfAppartments = listOf("Квартира", "Дом", "Коттедж", "Комната")

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
            val type = fragment_add_ad_type_of_appartment_drop_down_auto_tv.text.toString()

            if (type != "")
                navNextFragment(type)
            else
                Toast.makeText(requireContext(), "Выберите тип жилья", Toast.LENGTH_SHORT).show()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }

        val typesAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            typesOfAppartments)
        fragment_add_ad_type_of_appartment_drop_down_auto_tv.setAdapter(typesAdapter)

    }

    private fun navNextFragment(type: String){
        val bundle = bundleOf("typeOfApartment" to type)
        findNavController().navigate(R.id.action_profileAddTypeFragment_to_profileAddQuestsFragment, bundle)
    }
}

















