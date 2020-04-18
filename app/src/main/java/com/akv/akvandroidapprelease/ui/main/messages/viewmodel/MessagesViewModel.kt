package com.akv.akvandroidapprelease.ui.main.messages.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.MessagesRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.messages.state.MessagesStateEvent
import com.akv.akvandroidapprelease.ui.main.messages.state.MessagesViewState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapprelease.util.AbsentLiveData
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











