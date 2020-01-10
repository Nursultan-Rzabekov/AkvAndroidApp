package com.example.akvandroidapp.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
import com.example.akvandroidapp.api.main.responses.ChatHistoryResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.messages.state.MessagesViewState
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.GenericApiResponse
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MessagesRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("FavoriteRepository") {

    private val TAG: String = "AppDebug"

    fun myHouseList(
        authToken: AuthToken,
        uri:String
    ): LiveData<DataState<MessagesViewState>> {

        return object :
            NetworkBoundResource<ChatHistoryResponse, List<BlogPost>, MessagesViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                false,
                true
            ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<ChatHistoryResponse>
            ) {
                Log.d("qwe", "result count ${response.body.id}")
                Log.d("qwe", "result response ${response.body.uri}")

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = MessagesViewState(
                                MessagesViewState.MyChatFields(
                                    response.body.messages,
                                    response.body.uri,
                                    isQueryInProgress = false,
                                    isQueryExhausted = true
                                )
                            )
                        )
                    )
                }
            }
            override fun createCall(): LiveData<GenericApiResponse<ChatHistoryResponse>> {
                return openApiMainService.getChatHistory(
                    "Token ${authToken.token!!}",
                    uri
                )
            }
            override fun loadFromCache(): LiveData<MessagesViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }
            override fun setJob(job: Job) {
                addJob("searchBlogPosts", job)
            }
        }.asLiveData()
    }
}
















