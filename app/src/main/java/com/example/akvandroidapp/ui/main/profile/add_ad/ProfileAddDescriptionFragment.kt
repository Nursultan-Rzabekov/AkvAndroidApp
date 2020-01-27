package com.example.akvandroidapp.ui.main.profile.add_ad


import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import kotlinx.android.synthetic.main.back_button_layout.*
import kotlinx.android.synthetic.main.fragment_add_ad_description.*
import javax.inject.Inject


class ProfileAddDescriptionFragment : BaseAddHouseFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ad_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateContent()

        fragment_add_ad_description_next_btn.setOnClickListener {
            if (checkInputs())
                navNextFragment()
        }

        main_back_img_btn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navNextFragment(){
        sessionManager.setAddAdtitleAndDescription(
            fragment_add_ad_description_title_et.text.toString().trim(),
            fragment_add_ad_description_desc_et.text.toString().trim()
        )
        findNavController().navigate(R.id.action_profileAddDescriptionFragment_to_profileAddCheckFragment)
    }

    private fun initiateContent(){
        setSpans()
    }

    private fun setSpans(){
        val foregroundColor = ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primaryZero))

        fragment_add_ad_description_title_tv.setText(fragment_add_ad_description_title_tv.text.toString(), TextView.BufferType.SPANNABLE)
        val span1 = fragment_add_ad_description_title_tv.text as Spannable
        span1.setSpan(foregroundColor, 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        fragment_add_ad_description_desc_tv.setText(fragment_add_ad_description_desc_tv.text.toString(), TextView.BufferType.SPANNABLE)
        val span2 = fragment_add_ad_description_desc_tv.text as Spannable
        span2.setSpan(foregroundColor, 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun checkInputs(): Boolean{
        if (fragment_add_ad_description_title_et.text.toString().trim() != "" &&
            fragment_add_ad_description_desc_et.text.toString().trim() != "")
            return true
        if (fragment_add_ad_description_desc_et.text.toString().trim() == ""){
            fragment_add_ad_description_desc_l_et.isErrorEnabled = true
            fragment_add_ad_description_desc_l_et.error = getString(R.string.invalid)
        }
        if (fragment_add_ad_description_title_et.text.toString().trim() == ""){
            fragment_add_ad_description_title_l_et.isErrorEnabled = true
            fragment_add_ad_description_title_l_et.error = getString(R.string.invalid)
        }
        return false
    }
}

















