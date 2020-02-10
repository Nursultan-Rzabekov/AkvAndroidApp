package com.example.akvandroidapp.ui.main.profile.add_ad

import android.Manifest
import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.*
import com.example.akvandroidapp.ui.main.MainActivity
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_add_ad.*
import java.util.*
import javax.inject.Inject

class AddAdMainActivity : BaseLocationActivity(), NavController.OnDestinationChangedListener
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

        locationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        providers = locationManager.getProviders(true)

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionsToRequest = findUnAskedPermissions(permissions)

        if (!isGPS && !isNetwork) {
            showSettingsAlert(5)
            getLastLocation(locationManager)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size > 0) {
                    requestPermissions(
                        permissionsToRequest.toTypedArray(),
                        Constants.ALL_PERMISSIONS_RESULT
                    )
                    canGetLocation = false
                }
            }
            getLocation()
        }
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(this, androidx.lifecycle.Observer { dataState ->
            onDataStateChange(dataState)

        })
        viewModel.viewState.observe(this, androidx.lifecycle.Observer{
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: ${it}")

        })
        sessionManager.success.observe(this, androidx.lifecycle.Observer{ dataState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthDataState: ${dataState}")
            if(dataState == Constants.SUCCESS){
                navMainActivity()
            }
        })
    }


    fun navMainActivity(){
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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

















