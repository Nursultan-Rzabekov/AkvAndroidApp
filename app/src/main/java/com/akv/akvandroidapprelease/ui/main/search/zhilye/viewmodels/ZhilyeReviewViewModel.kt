package com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.SearchRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getHouseId
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeReviewsStateEvent
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.akv.akvandroidapprelease.util.AbsentLiveData
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

            is ZhilyeReviewsStateEvent.CreateReviewEvent -> {
                return sessionManager.cachedToken.value?.let {
                    Log.d(TAG, "houseId: ${stateEvent.houseId}, stars: ${stateEvent.stars}, body: ${stateEvent.body}")
                    searchRepository.addReviewForHouse(
                        authToken = it,
                        houseId = stateEvent.houseId,
                        stars = stateEvent.stars,
                        body = stateEvent.body
                    )
                }?: AbsentLiveData.create()
            }

            is ZhilyeReviewsStateEvent.UpdateReviewEvent -> {
                return sessionManager.cachedToken.value?.let {
                    Log.d(TAG, "houseId: ${stateEvent.houseId}, stars: ${stateEvent.stars}, body: ${stateEvent.body}")
                    searchRepository.updateReviewForHouse(
                        authToken = it,
                        houseId = stateEvent.houseId,
                        reviewId = stateEvent.reviewId,
                        body = stateEvent.body,
                        stars = stateEvent.stars
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











