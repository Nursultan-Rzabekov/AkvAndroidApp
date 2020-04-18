package com.akv.akvandroidapprelease.repository.main

import androidx.lifecycle.LiveData
import com.akv.akvandroidapprelease.api.main.GenericResponse
import com.akv.akvandroidapprelease.api.main.OpenApiMainService
import com.akv.akvandroidapprelease.api.main.responses.BlogListFavoritesResponse
import com.akv.akvandroidapprelease.entity.AuthToken
import com.akv.akvandroidapprelease.entity.BlogPost
import com.akv.akvandroidapprelease.persistence.BlogPostDao
import com.akv.akvandroidapprelease.repository.JobManager
import com.akv.akvandroidapprelease.repository.NetworkBoundResource
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.main.favorite.state.FavoriteViewState
import com.akv.akvandroidapprelease.util.AbsentLiveData
import com.akv.akvandroidapprelease.util.ApiSuccessResponse
import com.akv.akvandroidapprelease.util.Constants
import com.akv.akvandroidapprelease.util.GenericApiResponse
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
                            id = blogPostResponse.id,
                            name = blogPostResponse.name,
                            beds = blogPostResponse.beds,
                            rooms = blogPostResponse.rooms,
                            is_favourite = blogPostResponse.is_favourite,
                            longitude = blogPostResponse.longitude,
                            latitude = blogPostResponse.latitude,
                            house_type = blogPostResponse.house_type,
                            city = blogPostResponse.city,
                            price = blogPostResponse.price,
                            status = blogPostResponse.status,
                            image = "${Constants.BASE_URL_IMAGE}${blogPostResponse.photos?.first()?.image}",
                            rating = blogPostResponse.rating
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
                                    isQueryExhausted = booleanQuery(response.body.count)))
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostListSize: Int):Boolean{
                if(page * Constants.PAGINATION_PAGE_SIZE_FAVORITE >= blogPostListSize){
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
















