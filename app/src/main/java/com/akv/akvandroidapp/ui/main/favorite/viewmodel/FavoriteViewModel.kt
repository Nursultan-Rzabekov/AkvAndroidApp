package com.akv.akvandroidapp.ui.main.favorite.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapp.repository.main.FavoriteRepository
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.BaseViewModel
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.Loading
import com.akv.akvandroidapp.ui.main.favorite.state.FavoriteStateEvent
import com.akv.akvandroidapp.ui.main.favorite.state.FavoriteViewState
import com.akv.akvandroidapp.ui.main.search.viewmodel.getHouseId
import com.akv.akvandroidapp.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapp.util.AbsentLiveData
import javax.inject.Inject

class FavoriteViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val favoriteRepository: FavoriteRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<FavoriteStateEvent, FavoriteViewState>(){


    override fun handleStateEvent(stateEvent: FavoriteStateEvent): LiveData<DataState<FavoriteViewState>> {
        when(stateEvent){
            is FavoriteStateEvent.FavoriteMyListEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    favoriteRepository.getMyFavoritePosts(
                        authToken = authToken,
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is FavoriteStateEvent.DeleteFavoriteItemEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    favoriteRepository.deleteMyFavoritePosts(
                        authToken = authToken,
                        houseId = getHouseId()
                    )
                }?: AbsentLiveData.create()
            }

            is FavoriteStateEvent.None ->{
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


    override fun initNewViewState(): FavoriteViewState {
        return FavoriteViewState()
    }

    fun cancelActiveJobs(){
        favoriteRepository.cancelActiveJobs() // cancel active jobs
        handlePendingData() // hide progress bar
    }

    fun handlePendingData(){
        setStateEvent(FavoriteStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

}











