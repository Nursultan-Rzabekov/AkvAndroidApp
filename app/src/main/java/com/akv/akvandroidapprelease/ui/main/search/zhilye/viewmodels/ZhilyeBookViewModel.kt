package com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.SearchRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeBookStateEvent
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.akv.akvandroidapprelease.util.AbsentLiveData
import javax.inject.Inject


class ZhilyeBookViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<ZhilyeBookStateEvent, ZhilyeBookViewState>(){

    override fun handleStateEvent(stateEvent: ZhilyeBookStateEvent): LiveData<DataState<ZhilyeBookViewState>> {
        when(stateEvent){
            is ZhilyeBookStateEvent.ReservationEvent -> {
                return sessionManager.cachedToken.value?.let {
                    val _check_in = stateEvent.check_in
                    val _check_out = stateEvent.check_out
                    val _guests = stateEvent.guests
                    val _house_id = stateEvent.houseId

                    searchRepository.sendReservationRequest(
                        it,
                        check_in = _check_in,
                        check_out = _check_out,
                        guests = _guests,
                        house_id = _house_id
                    )
                }?: AbsentLiveData.create()
            }

            is ZhilyeBookStateEvent.None -> {
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

    override fun initNewViewState(): ZhilyeBookViewState {
        return ZhilyeBookViewState()
    }

    fun cancelActiveJobs(){
        searchRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(ZhilyeBookStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}











