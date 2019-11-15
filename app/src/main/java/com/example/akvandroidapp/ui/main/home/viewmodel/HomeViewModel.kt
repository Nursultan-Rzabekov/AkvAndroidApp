package com.example.akvandroidapp.ui.main.home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.repository.main.HomeRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.home.state.HomeStateEvent
import com.example.akvandroidapp.ui.main.home.state.HomeViewState
import javax.inject.Inject

class HomeViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val homeRepository: HomeRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<HomeStateEvent, HomeViewState>(){


    override fun handleStateEvent(stateEvent: HomeStateEvent): LiveData<DataState<HomeViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    fun cancelActiveJobs(){
        homeRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(HomeStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











