package com.example.akvandroidapp.repository.main

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.GenericResponse
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.BlogListFavoritesResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.favorite.state.FavoriteViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.Constants
import com.example.akvandroidapp.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("FavoriteRepository") {

    private val TAG: String = "AppDebug"

    fun getMyFavoritePosts(
        authToken: AuthToken,
        page: Int
    ): LiveData<DataState<FavoriteViewState>> {

        return object: NetworkBoundResource<BlogListFavoritesResponse, List<BlogPost>, FavoriteViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<BlogListFavoritesResponse>
            ) {

                val blogPostList: ArrayList<BlogPost> = ArrayList()
                for(blogPostResponse in response.body.results){
                    blogPostList.add(
                        BlogPost(
                            id = blogPostResponse.house.id,
                            name = blogPostResponse.house.name,
                            beds = blogPostResponse.house.beds,
                            rooms = blogPostResponse.house.rooms,
                            is_favourite = blogPostResponse.house.is_favourite,
                            longitude = blogPostResponse.house.longitude,
                            latitude = blogPostResponse.house.latitude,
                            house_type = blogPostResponse.house.house_type,
                            city = blogPostResponse.house.city,
                            price = blogPostResponse.house.price,
                            status = blogPostResponse.house.status,
                            image = "https://akv-technopark.house.herokuapp.com${blogPostResponse.house.photos?.first()?.image}",
                            rating = blogPostResponse.house.rating
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = FavoriteViewState(
                                FavoriteViewState.FavoriteFields(
                                    blogPostList,
                                    isQueryInProgress = false,
                                    isQueryExhausted = booleanQuery(blogPostList)))
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostList: ArrayList<BlogPost>):Boolean{
                if(page * Constants.PAGINATION_PAGE_SIZE > blogPostList.size){
                    return true
                }
                return false
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListFavoritesResponse>> {
                return openApiMainService.getMyFavorites(
                    "Token ${authToken.token!!}",
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<FavoriteViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }

            override fun setJob(job: Job) {
                addJob("favoritePosts", job)
            }

        }.asLiveData()
    }



    fun deleteMyFavoritePosts(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<FavoriteViewState>> {

        return object: NetworkBoundResource<GenericResponse, List<BlogPost>, FavoriteViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<GenericResponse>
            ) {
                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = FavoriteViewState( deleteblogFields =
                                FavoriteViewState.FavoriteDeleteFields(response.body.response))
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.deleteFavoritePost(
                    "Token ${authToken.token!!}",
                    house_id = houseId
                )
            }

            override fun loadFromCache(): LiveData<FavoriteViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }

            override fun setJob(job: Job) {
                addJob("favoritePosts", job)
            }

        }.asLiveData()
    }
}
















