package com.akv.akvandroidapprelease.ui.main.messages.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.MessagesRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.messages.state.RequestStateEvent
import com.akv.akvandroidapprelease.ui.main.messages.state.RequestViewState
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getOrdersPage
import com.akv.akvandroidapprelease.util.AbsentLiveData
import javax.inject.Inject

class RequestViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val messagesRepository: MessagesRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<RequestStateEvent, RequestViewState>(){

    override fun handleStateEvent(stateEvent: RequestStateEvent): LiveData<DataState<RequestViewState>> {
        when(stateEvent){
            is RequestStateEvent.OrdersListStateEvent -> {
                return sessionManager.cachedToken.value?.let {
                    messagesRepository.ordersList(
                        it,
                        page = getOrdersPage()
                    )
                }?: AbsentLiveData.create()
            }

            is RequestStateEvent.None -> {
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

            is RequestStateEvent.AcceptReservationEvent -> {
                return sessionManager.cachedToken.value?.let {
                    messagesRepository.acceptReservation(
                        it,
                        reservation_id = stateEvent.reservation_id
                    )
                }?: AbsentLiveData.create()
            }

            is RequestStateEvent.RejectReservationEvent -> {
                return sessionManager.cachedToken.value?.let {
                    messagesRepository.rejectReservation(
                        it,
                        reservation_id = stateEvent.reservation_id
                    )
                }?: AbsentLiveData.create()
            }

        }
    }

    override fun initNewViewState(): RequestViewState {
        return RequestViewState()
    }

//    fun cancelActiveJobs(){
//        messagesRepository.cancelActiveJobs() // cancel active jobs
//        handlePendingData() // hide progress bar
//    }

    fun handlePendingData(){
        setStateEvent(RequestStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        //cancelActiveJobs()
    }

}











