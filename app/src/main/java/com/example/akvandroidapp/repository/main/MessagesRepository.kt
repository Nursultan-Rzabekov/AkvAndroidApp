package com.example.akvandroidapp.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.AllChatsResponse
import com.example.akvandroidapp.api.main.responses.ConverstaionsResponse
import com.example.akvandroidapp.api.main.responses.UserConversationsInfoResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.UserChatMessages
import com.example.akvandroidapp.entity.UserConversationMessages
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewState
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject

class MessagesRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("MessagesRepository") {

    private val TAG: String = "AppDebug"

    fun myChatsList(
        authToken: AuthToken,
        page:Int
    ): LiveData<DataState<MessagesViewState>> {
        return object :
            NetworkBoundResource<AllChatsResponse, List<BlogPost>, MessagesViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<AllChatsResponse>
            ) {
                Log.d("qwe", "result count ${response.body.count}")
                Log.d("qwe", "result response ${response.body.results}")


                val blogPostList: ArrayList<UserChatMessages> = ArrayList()
                for(blogPostResponse in response.body.results){
                    blogPostList.add(
                        UserChatMessages(
                            id = blogPostResponse.id,
                            email = blogPostResponse.email,
                            first_name = blogPostResponse.first_name,
                            last_name = blogPostResponse.last_name,
                            userpic = blogPostResponse.userpic
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = MessagesViewState(
                                MessagesViewState.MyChatFields(
                                    blogPostList,
                                    isQueryInProgress = false,
                                    isQueryExhausted = true
                                )
                            )
                        )
                    )
                }

            }
            override fun createCall(): LiveData<GenericApiResponse<AllChatsResponse>> {
                Log.d("wqe","loadChat + ${page}")
                return openApiMainService.getAllChats(
                    "Token ${authToken.token!!}",
                    page
                )
            }
            override fun loadFromCache(): LiveData<MessagesViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }
            override fun setJob(job: Job) {
                addJob("messagesBlogPosts", job)
            }
        }.asLiveData()
    }

    fun myConversationsList(
        authToken: AuthToken,
        target: String,
        page:Int
    ): LiveData<DataState<DetailsViewState>> {
        return object :
            NetworkBoundResource<ConverstaionsResponse, List<BlogPost>, DetailsViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<ConverstaionsResponse>
            ) {
                Log.d("qwe", "result count ${response.body.count}")
                Log.d("qwe", "result response ${response.body.results}")


                val blogPostList: ArrayList<UserConversationMessages> = ArrayList()
                for(blogPostResponse in response.body.results){
                    blogPostList.add(
                        UserConversationMessages(
                            id = blogPostResponse.id,
                            user = blogPostResponse.user,
                            recipient = blogPostResponse.recipient,
                            body = blogPostResponse.body,
                            created_at = blogPostResponse.created_at,
                            updated_at = blogPostResponse.updated_at
                        )
                    )
                }

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = DetailsViewState(
                                DetailsViewState.MyChatDetailsFields(
                                    blogPostList,
                                    isQueryInProgress = false,
                                    isQueryExhausted = true
                                )
                            )
                        )
                    )
                }

            }
            override fun createCall(): LiveData<GenericApiResponse<ConverstaionsResponse>> {
                Log.d("wqe","loadChat + ${page}")
                return openApiMainService.getConversations(
                    "Token ${authToken.token!!}",
                    target,
                    page
                )
            }
            override fun loadFromCache(): LiveData<DetailsViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }
            override fun setJob(job: Job) {
                addJob("messagesBlogPosts", job)
            }
        }.asLiveData()
    }

    fun sendMessage(
        authToken: AuthToken,
        recipient: RequestBody,
        body: RequestBody
    ): LiveData<DataState<DetailsViewState>>{
        return object:
            NetworkBoundResource<UserConversationsInfoResponse, List<BlogPost>, DetailsViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<UserConversationsInfoResponse>
            ) {
                Log.d("send message", "recipient ${response.body.recipient}")

                val sendMessageInfo = UserConversationMessages(
                    id = response.body.id,
                    user = response.body.user,
                    recipient = response.body.recipient,
                    body = response.body.body,
                    created_at = response.body.created_at,
                    updated_at = response.body.updated_at
                )

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = DetailsViewState(
                                sendMessageFields = DetailsViewState.SendMessageFields(
                                    blogPost = sendMessageInfo
                                )
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<UserConversationsInfoResponse>> {
                Log.d("send message call", "send message $recipient")
                return openApiMainService.sendMessageTo(
                    "Token ${authToken.token!!}",
                    recipient,
                    body
                )
            }

            override fun loadFromCache(): LiveData<DetailsViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("messagesSend", job)
            }
        }.asLiveData()
    }
}
















