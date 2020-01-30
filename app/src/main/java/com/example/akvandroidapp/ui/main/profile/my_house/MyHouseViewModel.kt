package com.example.akvandroidapp.ui.main.profile.my_house


import android.util.Log
import androidx.lifecycle.*
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.search.viewmodel.getHouseId
import com.example.akvandroidapp.ui.main.search.viewmodel.getPage
import com.example.akvandroidapp.ui.main.search.viewmodel.getState
import com.example.akvandroidapp.util.AbsentLiveData
import javax.inject.Inject


class MyHouseViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
): BaseViewModel<MyHouseStateStateEvent, MyHouseViewState>()
{
    override fun handleStateEvent(stateEvent: MyHouseStateStateEvent): LiveData<DataState<MyHouseViewState>> {
        when(stateEvent){

            is MyHouseStateStateEvent.MyHouseEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    profileRepository.myHouseList(
                        authToken = authToken,
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is MyHouseStateStateEvent.MyHouseStateEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    Log.d("Go", "Go go go go  + ${getState()}")
                    Log.d("Go", "Go go go go  + ${getHouseId()}")
                    if(getState() == 0){
                        profileRepository.myHouseStateActivate(
                            authToken = authToken,
                            houseId = getHouseId()
                        )
                    }
                    else{
                        profileRepository.myHouseStateDeactivate(
                            authToken = authToken,
                            houseId = getHouseId()
                        )
                    }
                }?: AbsentLiveData.create()
            }


            is MyHouseStateStateEvent.None ->{
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

    override fun initNewViewState(): MyHouseViewState {
        return MyHouseViewState()
    }

    fun cancelActiveJobs(){
        handlePendingData()
        profileRepository.cancelActiveJobs()
    }

    fun handlePendingData(){
        setStateEvent(MyHouseStateStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































