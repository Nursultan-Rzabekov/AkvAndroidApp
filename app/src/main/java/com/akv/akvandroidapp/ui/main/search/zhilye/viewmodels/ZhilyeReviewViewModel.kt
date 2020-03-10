package com.akv.akvandroidapp.ui.main.search.zhilye.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapp.repository.main.SearchRepository
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.BaseViewModel
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.Loading
import com.akv.akvandroidapp.ui.main.search.viewmodel.getHouseId
import com.akv.akvandroidapp.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsStateEvent
import com.akv.akvandroidapp.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.akv.akvandroidapp.util.AbsentLiveData
import javax.inject.Inject

class ZhilyeReviewViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<ZhilyeReviewsStateEvent, ZhilyeReviewsViewState>(){

    override fun handleStateEvent(stateEvent: ZhilyeReviewsStateEvent): LiveData<DataState<ZhilyeReviewsViewState>> {
        when(stateEvent){
            is ZhilyeReviewsStateEvent.ZhilyeReviewsEvent -> {
                return sessionManager.cachedToken.value?.let {
                    searchRepository.getReviewsForHouse(
                        house_id = getHouseId(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is ZhilyeReviewsStateEvent.None -> {
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

    override fun initNewViewState(): ZhilyeReviewsViewState {
        return ZhilyeReviewsViewState()
    }

    fun cancelActiveJobs(){
        searchRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(ZhilyeReviewsStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}











