package com.example.akvandroidapp.ui.main.favorite.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.repository.main.FavoriteRepository
import com.example.akvandroidapp.repository.main.HomeRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteStateEvent
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteViewState
import javax.inject.Inject

class FavoriteViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val favoriteRepository: FavoriteRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<FavoriteStateEvent, FavoriteViewState>(){


    override fun handleStateEvent(stateEvent: FavoriteStateEvent): LiveData<DataState<FavoriteViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun initNewViewState(): FavoriteViewState {
        return FavoriteViewState()
    }

    fun cancelActiveJobs(){
        favoriteRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(FavoriteStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











