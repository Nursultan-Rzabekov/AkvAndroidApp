package com.example.akvandroidapp.ui.main.home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.HomeRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.home.state.HomeStateEvent
import com.example.akvandroidapp.ui.main.home.state.HomeViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.getPage
import com.example.akvandroidapp.util.AbsentLiveData
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
        when(stateEvent){
            is HomeStateEvent.HomeInfoEvent -> {
                return sessionManager.cachedToken.value?.let {
                    homeRepository.getReservations(
                        authToken = it,
                        page = getPage()
                    )
                }?:AbsentLiveData.create()
            }
            is HomeStateEvent.HomeCancelReservationEvent -> {
                return sessionManager.cachedToken.value?.let {
                    homeRepository.cancelReservation(
                        authToken = it,
                        reservation_id = stateEvent.reservation_id,
                        messageOne = stateEvent.message
                    )
                }?:AbsentLiveData.create()
            }
            is HomeStateEvent.HomePayReservationEvent -> {
                return sessionManager.cachedToken.value?.let {
                    homeRepository.payReservationId(
                        authToken = it,
                        reservation_id = stateEvent.reservation_id
                    )
                }?:AbsentLiveData.create()
            }
            is HomeStateEvent.None -> {
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


    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    fun cancelActiveJobs(){
        homeRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(HomeStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











