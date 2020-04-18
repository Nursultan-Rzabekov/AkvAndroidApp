package com.akv.akvandroidapprelease.ui.main.search.zhilye

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.SearchRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.getHouseId
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeStateEvent
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeViewState
import com.akv.akvandroidapprelease.util.AbsentLiveData
import javax.inject.Inject

class ZhilyeViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val searchRepository: SearchRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<ZhilyeStateEvent, ZhilyeViewState>(){

    override fun handleStateEvent(stateEvent: ZhilyeStateEvent): LiveData<DataState<ZhilyeViewState>> {
        when(stateEvent){
            is ZhilyeStateEvent.BlogZhilyeEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.getZhilyeWithHouseId(
                        authToken = authToken,
                        houseId = getHouseId()
                    )
                }?: AbsentLiveData.create()
            }


            is ZhilyeStateEvent.DeleteFavoriteItemEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.deleteMyFavoritePosts(
                        authToken = authToken,
                        houseId = getHouseId()
                    )
                }?: AbsentLiveData.create()
            }

            is ZhilyeStateEvent.CreateFavoriteItemEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    searchRepository.createMyFavoritePosts(
                        authToken = authToken,
                        houseId = getHouseId()
                    )
                }?: AbsentLiveData.create()
            }

            is ZhilyeStateEvent.None ->{
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

    override fun initNewViewState(): ZhilyeViewState {
        return ZhilyeViewState()
    }

    fun cancelActiveJobs(){
        searchRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(ZhilyeStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}











