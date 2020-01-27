package com.example.akvandroidapp.ui.main.messages.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.MessagesRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.messages.state.MessagesStateEvent
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import com.example.akvandroidapp.ui.main.profile.viewmodel.BlockedDates
import com.example.akvandroidapp.ui.main.search.viewmodel.getOrdersPage
import com.example.akvandroidapp.ui.main.search.viewmodel.getPage
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.Constants
import okhttp3.MediaType
import okhttp3.RequestBody
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

            is MessagesStateEvent.OrdersListStateEvent -> {
                return sessionManager.cachedToken.value?.let {
                    messagesRepository.ordersList(
                        it,
                        page = getOrdersPage()
                    )
                }?: AbsentLiveData.create()
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
        setStateEvent(MessagesStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        //cancelActiveJobs()
    }

}











