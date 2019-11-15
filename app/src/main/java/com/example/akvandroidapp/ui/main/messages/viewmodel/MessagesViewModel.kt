package com.example.akvandroidapp.ui.main.messages.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.repository.main.MessagesRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.messages.state.MessagesStateEvent
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import javax.inject.Inject

class MessagesViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val messagesRepository: MessagesRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<MessagesStateEvent, MessagesViewState>(){


    override fun handleStateEvent(stateEvent: MessagesStateEvent): LiveData<DataState<MessagesViewState>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun initNewViewState(): MessagesViewState {
        return MessagesViewState()
    }

    fun cancelActiveJobs(){
        messagesRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(MessagesStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











