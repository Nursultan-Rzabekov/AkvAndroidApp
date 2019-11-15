package com.example.akvandroidapp.ui.main.favorite

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.example.akvandroidapp.ui.DataStateChangeListener
import com.example.akvandroidapp.ui.UICommunicationListener
import com.example.akvandroidapp.ui.main.favorite.viewmodel.FavoriteViewModel
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFavoriteFragment : DaggerFragment(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var requestManager: RequestManager

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var uiCommunicationListener: UICommunicationListener

    lateinit var stateChangeListener: DataStateChangeListener

    lateinit var viewModel: FavoriteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(FavoriteViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        cancelActiveJobs()
    }

    fun cancelActiveJobs(){
        viewModel.cancelActiveJobs()
    }

}