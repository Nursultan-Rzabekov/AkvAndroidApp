package com.example.akvandroidapp.ui.main.profile.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<ProfileStateEvent, ProfileViewState>(){


    override fun handleStateEvent(stateEvent: ProfileStateEvent): LiveData<DataState<ProfileViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun initNewViewState(): ProfileViewState {
        return ProfileViewState()
    }

    fun cancelActiveJobs(){
        profileRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(ProfileStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











