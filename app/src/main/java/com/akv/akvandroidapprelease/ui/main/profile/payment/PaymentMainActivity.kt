package com.akv.akvandroidapprelease.ui.main.profile.payment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.ui.BaseActivity
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentViewModel
import com.akv.akvandroidapprelease.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_support_main.*
import javax.inject.Inject

class PaymentMainActivity : BaseActivity(), NavController.OnDestinationChangedListener
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

    lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments_main)

        viewModel = ViewModelProvider(this, providerFactory).get(PaymentViewModel::class.java)
        findNavController(R.id.payment_nav_host_fragment).addOnDestinationChangedListener(this)

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

















