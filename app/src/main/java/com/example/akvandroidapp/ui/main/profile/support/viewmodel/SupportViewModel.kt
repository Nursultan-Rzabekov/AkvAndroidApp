package com.example.akvandroidapp.ui.main.profile.support.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.ProfileRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.util.AbsentLiveData
import okhttp3.MediaType
import okhttp3.RequestBody
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
            is SupportStateEvent.FeedbackSendEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    val _message = RequestBody.create(MediaType.parse("text/plain"), stateEvent.message)
                    profileRepository.sendFeedback(
                        authToken,
                        _message
                    )
                }?: AbsentLiveData.create()
            }

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
































