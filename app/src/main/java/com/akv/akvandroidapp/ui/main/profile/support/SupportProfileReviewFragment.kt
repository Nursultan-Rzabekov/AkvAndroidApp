package com.akv.akvandroidapp.ui.main.profile.support


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.akv.akvandroidapp.R
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.main.profile.support.viewmodel.SupportStateEvent
import com.akv.akvandroidapp.ui.main.profile.support.viewmodel.SupportViewState
import com.akv.akvandroidapp.util.ErrorHandling
import handleFeedback
import kotlinx.android.synthetic.main.fragment_support_notify_error.*
import kotlinx.android.synthetic.main.support_main_review_layout.*


class SupportProfileReviewFragment : BaseSupportFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.support_main_review_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        setHasOptionsMenu(true)
        Log.d(TAG, "SearchFragment: ${viewModel}")

        setToobar()

        fragment_support_notify_error_btn.setOnClickListener {
            if (fragment_support_notify_error_et.text.toString().trim().isNotBlank()) {
                subscribeObservers()
                sendFeedback(fragment_support_notify_error_et.text.toString().trim())
            }
        }
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer{ dataState ->
            if(dataState != null) {
                handleFeedback(dataState)
                stateChangeListener.onDataStateChange(dataState)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null){
                if (viewState.id >= 0)
                    findNavController().navigateUp()
            }
        })
    }

    private fun handleFeedback(dataState: DataState<SupportViewState>){
        dataState.data?.let {
            it.data?.let{
                it.getContentIfNotHandled()?.let{
                    viewModel.handleFeedback(it)
                }
            }
        }
        dataState.error?.let{ event ->
            event.peekContent().response.message?.let{
                if(ErrorHandling.isPaginationDone(it)){
                    // handle the error message event so it doesn't display in UI
                    event.getContentIfNotHandled()
                }
            }
        }
    }

    private fun setToobar(){
        header_support_message_toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)

        header_support_message_toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendFeedback(message: String){
        viewModel.setStateEvent(
            SupportStateEvent.FeedbackSendEvent(message)
        )
    }
}