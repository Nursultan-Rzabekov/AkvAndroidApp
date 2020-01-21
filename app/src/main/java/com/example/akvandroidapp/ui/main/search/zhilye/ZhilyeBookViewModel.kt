package com.example.akvandroidapp.ui.main.search.zhilye

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.SearchRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.search.state.SearchStateEvent
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.viewmodel.getHouseId
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookStateEvent
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_BEDS_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_BEDS_RIGHT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_PRICE_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_PRICE_RIGHT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_ROOMS_LEFT
import com.example.akvandroidapp.util.PreferenceKeys.Companion.Search_FILTER_ROOMS_RIGHT
import okhttp3.MediaType
import okhttp3.RequestBody
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
                    val _check_in = RequestBody.create(MediaType.parse("text/plain"), stateEvent.check_in)
                    val _check_out = RequestBody.create(MediaType.parse("text/plain"), stateEvent.check_out)
                    val _guests = RequestBody.create(MediaType.parse("text/plain"), stateEvent.guests.toString())
                    val _house_id = RequestBody.create(MediaType.parse("text/plain"), stateEvent.houseId.toString())

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
        setStateEvent(ZhilyeBookStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}










