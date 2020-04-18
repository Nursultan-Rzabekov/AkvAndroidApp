package com.akv.akvandroidapprelease.ui.main.messages.detailState

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akv.akvandroidapprelease.repository.main.MessagesRepository
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.BaseViewModel
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Loading
import com.akv.akvandroidapprelease.ui.main.search.viewmodel.*
import com.akv.akvandroidapprelease.util.AbsentLiveData
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

    override fun handleStateEvent(stateEvent: DetailsStateEvent): LiveData<DataState<DetailsViewState>> {
        when(stateEvent){
            is DetailsStateEvent.ChatDetailEvent -> {

                Log.d(TAG, "result response ${getBlogList().size}")
                return sessionManager.cachedToken.value?.let { authToken ->
                    messagesRepository.myConversationsList(
                        authToken = authToken,
                        target = getTargetQuery(),
                        page = getPage()
                    )
                }?: AbsentLiveData.create()
            }


            is DetailsStateEvent.SendMessageEvent -> {
                return sessionManager.cachedToken.value?.let {
                    val _recipient = RequestBody.create(MediaType.parse("text/plain"), getEmailName().toString())
                    val _body = RequestBody.create(MediaType.parse("text/plain"), getMessageBody())

                    messagesRepository.sendMessage(
                        it,
                        recipient = _recipient,
                        body = _body,
                        photos = getMessageImages()
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
        setStateEvent(DetailsStateEvent.None)
    }

    override fun onCleared() {
        super.onCleared()
        //cancelActiveJobs()
    }

}











