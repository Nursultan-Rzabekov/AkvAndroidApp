package com.example.akvandroidapp.ui.main.profile.add_ad

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_add_ad.*
import java.util.*
import javax.inject.Inject

class AddAdMainActivity : BaseActivity(), NavController.OnDestinationChangedListener
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

    lateinit var viewModel: AddAdViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ad)

        Locale.setDefault(Locale.forLanguageTag("ru"))

        viewModel = ViewModelProvider(this, providerFactory).get(AddAdViewModel::class.java)
        findNavController(R.id.add_ad_nav_host_fragment).addOnDestinationChangedListener(this)

        //subscribeObservers()
    }

//    private fun subscribeObservers(){
//        viewModel.dataState.observe(this, Observer { dataState ->
//            onDataStateChange(dataState)
//
//        })
//
//        viewModel.viewState.observe(this, Observer{
//            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: ${it}")
//
//        })
//
//
//
////        sessionManager.cachedToken.observe(this, Observer{ dataState ->
////            Log.d(TAG, "AuthActivity, subscribeObservers: AuthDataState: ${dataState}")
////            dataState.let{ authToken ->
////                if(authToken?.token != null){
////                    navMainActivity()
////                }
////            }
////        })
//    }


//    fun navMainActivity(){
//        Log.d(TAG, "navMainActivity: called.")
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

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

















