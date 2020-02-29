package com.example.akvandroidapp.ui.main.profile.support

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.main.profile.support.viewmodel.SupportViewModel
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_support_main.*
import javax.inject.Inject

class SupportMainActivity : BaseActivity(), NavController.OnDestinationChangedListener
{
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewModel.cancelActiveJobs()
    }

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: SupportViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support_main)

        viewModel = ViewModelProvider(this, providerFactory).get(SupportViewModel::class.java)
        findNavController(R.id.support_nav_host_fragment).addOnDestinationChangedListener(this)
    }

    override fun displayProgressBar(bool: Boolean){
        if(bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }

    override fun expandAppBar() {
        // ignore
    }
}

















