package com.example.akvandroidapp.ui.main.profile.support.viewmodel


import androidx.lifecycle.*
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import javax.inject.Inject


class SupportViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
): BaseViewModel<SupportStateEvent, SupportViewState>()
{
    override fun handleStateEvent(stateEvent: SupportStateEvent): LiveData<DataState<SupportViewState>> {
        when(stateEvent){


            is SupportStateEvent.None ->{
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

    override fun initNewViewState(): SupportViewState {
        return SupportViewState()
    }

    fun cancelActiveJobs(){
        handlePendingData()
        profileRepository.cancelActiveJobs()
    }

    fun handlePendingData(){
        setStateEvent(SupportStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































