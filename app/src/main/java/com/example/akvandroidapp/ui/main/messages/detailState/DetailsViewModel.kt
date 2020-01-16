package com.example.akvandroidapp.ui.main.messages.detailState

import android.content.SharedPreferences
import android.os.IBinder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.example.akvandroidapp.repository.main.MessagesRepository
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.BaseViewModel
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Loading
import com.example.akvandroidapp.ui.main.search.viewmodel.getPage
import com.example.akvandroidapp.ui.main.search.viewmodel.getTargetQuery
import com.example.akvandroidapp.util.AbsentLiveData
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class DetailsViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val messagesRepository: MessagesRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<DetailsStateEvent, DetailsViewState>(){

    fun sendMessage(recipient: String, body: String){
        sessionManager.cachedToken.value?.let {
            val _recipient = RequestBody.create(MediaType.parse("text/plain"), recipient)
            val _body = RequestBody.create(MediaType.parse("text/plain"), body)

            messagesRepository.sendMessage(
                it,
                recipient = _recipient,
                body = _body
            )
        }
    }

    override fun handleStateEvent(stateEvent: DetailsStateEvent): LiveData<DataState<DetailsViewState>> {
        when(stateEvent){
            is DetailsStateEvent.ChatDetailEvent -> {
                return sessionManager.cachedToken.value?.let { authToken ->
                    messagesRepository.myConversationsList(
                        authToken = authToken,
                        target = getTargetQuery(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }

            is DetailsStateEvent.None -> {
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

    override fun initNewViewState(): DetailsViewState {
        return DetailsViewState()
    }

//    fun cancelActiveJobs(){
//        messagesRepository.cancelActiveJobs() // cancel active jobs
//        handlePendingData() // hide progress bar
//    }

    fun handlePendingData(){
        setStateEvent(DetailsStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        //cancelActiveJobs()
    }

}











