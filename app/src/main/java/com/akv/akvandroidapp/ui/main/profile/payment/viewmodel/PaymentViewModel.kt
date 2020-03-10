package com.akv.akvandroidapp.ui.main.profile.payment.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapp.repository.main.ProfileRepository
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.BaseViewModel
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.Loading
import com.akv.akvandroidapp.ui.main.search.viewmodel.getPage
import com.akv.akvandroidapp.util.AbsentLiveData
import javax.inject.Inject


class PaymentViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val profileRepository: ProfileRepository
): BaseViewModel<PaymentStateEvent, PaymentViewState>()
{
    override fun handleStateEvent(stateEvent: PaymentStateEvent): LiveData<DataState<PaymentViewState>> {
        when(stateEvent){

            is PaymentStateEvent.PaymentHistoryEvent -> {
                return sessionManager.cachedToken.value?.let {
                    profileRepository.getPaymentsHistory(
                        it,
                        getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is PaymentStateEvent.None ->{
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

    override fun initNewViewState(): PaymentViewState {
        return PaymentViewState()
    }

    fun cancelActiveJobs(){
        handlePendingData()
        profileRepository.cancelActiveJobs()
    }

    fun handlePendingData(){
        setStateEvent(PaymentStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}
































