package com.akv.akvandroidapprelease.ui.main.messages

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.akv.akvandroidapprelease.ui.DataStateChangeListener
import com.akv.akvandroidapprelease.ui.UICommunicationListener
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.RequestViewModel
import com.akv.akvandroidapprelease.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseRequestFragment : DaggerFragment(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var stateChangeListener: DataStateChangeListener

    lateinit var viewModel: RequestViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(RequestViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        cancelActiveJobs()
    }

    fun cancelActiveJobs(){
        //viewModel.cancelActiveJobs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }

        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }
    }

}