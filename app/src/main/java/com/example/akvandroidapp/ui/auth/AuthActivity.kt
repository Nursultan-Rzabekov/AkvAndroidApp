package com.example.akvandroidapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.ResponseType
import com.example.akvandroidapp.ui.auth.state.AuthStateEvent
import com.example.akvandroidapp.ui.main.MainActivity
import com.example.akvandroidapp.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import com.example.akvandroidapp.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener
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

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)

        subscribeObservers()

    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers(){

        viewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            Log.d(TAG, "AuthActivity, DataState: ${it}")
                            viewModel.setAuthToken(it)
                        }
                    }
                }
                data.response?.let{event ->
                    event.peekContent().let{ response ->
                        response.message?.let{ message ->
                            if(message.equals(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE)){
                                onFinishCheckPreviousAuthUser()
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(this, Observer{
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthViewState: ${it}")
            it.authToken?.let{
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer{ dataState ->
            Log.d(TAG, "AuthActivity, subscribeObservers: AuthDataState: ${dataState}")
            dataState.let{ authToken ->
                if(authToken?.token != null){
                    navMainActivity()
                }
            }
        })
    }

    fun navMainActivity(){
        Log.d(TAG, "navMainActivity: called.")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkPreviousAuthUser(){
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent())
    }

    private fun onFinishCheckPreviousAuthUser(){
        fragment_container.visibility = View.VISIBLE
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

















