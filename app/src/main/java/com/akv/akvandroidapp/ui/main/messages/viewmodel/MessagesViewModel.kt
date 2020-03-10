package com.akv.akvandroidapp.ui.main.messages.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapp.repository.main.MessagesRepository
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.BaseViewModel
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.Loading
import com.akv.akvandroidapp.ui.main.messages.state.MessagesStateEvent
import com.akv.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.akv.akvandroidapp.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapp.util.AbsentLiveData
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
        when(stateEvent){
            is MessagesStateEvent.ChatInfoEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    messagesRepository.myChatsList(
                        authToken = authToken,
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is MessagesStateEvent.None -> {
                return liveData {
                    emit(
                        DataState(
                            null,
                            Loading(false),
                            null
                        )
                    )
                }
            }
        }
    }

    override fun initNewViewState(): MessagesViewState {
        return MessagesViewState()
    }

//    fun cancelActiveJobs(){
//        messagesRepository.cancelActiveJobs() // cancel active jobs
//        handlePendingData() // hide progress bar
//    }

    fun handlePendingData(){
        setStateEvent(MessagesStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        //cancelActiveJobs()
    }

}











