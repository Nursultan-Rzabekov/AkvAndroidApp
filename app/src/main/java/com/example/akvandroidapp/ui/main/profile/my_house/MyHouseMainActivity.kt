package com.example.akvandroidapp.ui.main.profile.my_house

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_my_house_main.*
import java.util.*
import javax.inject.Inject

class MyHouseMainActivity : BaseActivity(), NavController.OnDestinationChangedListener
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

    lateinit var viewModel: MyHouseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_house_main)
        Locale.setDefault(Locale.forLanguageTag("ru"))

        viewModel = ViewModelProvider(this, providerFactory).get(MyHouseViewModel::class.java)
        findNavController(R.id.my_house_nav_host_fragment).addOnDestinationChangedListener(this)

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

















