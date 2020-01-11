package com.example.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.BlogCreateUpdateResponse
import com.example.akvandroidapp.api.main.responses.BlogGetProfileInfoResponse
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.ProfileInfo
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Response
import com.example.akvandroidapp.ui.ResponseType
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.profile.viewmodel.BlockedDates
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.util.*
import com.example.akvandroidapp.util.SuccessHandling.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("ProfileRepository") {

    private val TAG: String = "AppDebug"

    fun createNewBlogPost(
        authToken: AuthToken,
        name: RequestBody,
        description: RequestBody,
        rooms:RequestBody,
        address:RequestBody,
        longitude:RequestBody,
        latitude:RequestBody,
        city_id:RequestBody,
        price:RequestBody,
        beds:RequestBody,
        guests:RequestBody,
        rules:RequestBody,
        near_buildings:RequestBody,
        blocked_dates:RequestBody,
        photos: ArrayList<MultipartBody.Part>?,
        house_type_id:RequestBody,
        accommodations:RequestBody,
        discount7days:RequestBody,
        discount30days:RequestBody
    ): LiveData<DataState<ProfileViewState>> {
        return object :
            NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, ProfileViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                true,
                false
            ) {

            // not applicable
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<BlogCreateUpdateResponse>) {

                Log.d(TAG,"PostCreateHouse 444444 + ${response.body.name}")

//                if (!response.body.response.equals(RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER)) {
//                    val updatedBlogPost = BlogPost(
//                        response.body.pk,
//                        response.body.title,
//                        response.body.slug,
//                        response.body.body,
//                        response.body.image,
//                        DateUtils.convertServerStringDateToLong(response.body.date_updated),
//                        response.body.username
//                    )
//                    updateLocalDb(updatedBlogPost)
//                }

                withContext(Dispatchers.Main) {
                    // finish with success response
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(response.body.id.toString(), ResponseType.Dialog())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> {
                Log.d(TAG,"PostCreateHouse 3333333 + ${name}")
                return openApiMainService.createBlog(
//                    "application/json",
                    "Token ${authToken.token!!}",
                    name,
                    description,
                    rooms,
                    RequestBody.create(MediaType.parse("text/plain"), 4.toString()),
                    address,
                    longitude,
                    latitude,
                    city_id,
                    price,
                    beds,
                    guests,
                    rules,
                    near_buildings,
                    house_type_id,
                    blocked_dates,
                    accommodations,
                    discount7days,
                    discount30days,
                    photos
                )
            }

            // not applicable
            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let {
                    //blogPostDao.insert(it)
                }
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }

        }.asLiveData()
    }


    fun createGetProfileInfo(
        authToken: AuthToken
    ): LiveData<DataState<ProfileViewState>> {
        return object :
            NetworkBoundResource<BlogGetProfileInfoResponse, BlogPost, ProfileViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                true,
                false
            ) {

            // not applicable
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<BlogGetProfileInfoResponse>) {

                Log.d(TAG,"PostCreateHouse 444444 + ${response.body.phone}")

                sessionManager.setProfileInfo(
                    response.body.first_name,
                    response.body.birth_day,
                    response.body.gender,
                    response.body.phone,
                    response.body.email)

                withContext(Dispatchers.Main) {
                    // finish with success response
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(response.body.id.toString(), ResponseType.Dialog())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>> {
                Log.d(TAG,"PostCreateHouse 7070707070 + ${authToken}")
                return openApiMainService.getProfileInfo(
                    "Token ${authToken.token!!}"
                )
            }

            // not applicable
            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let {
                    //blogPostDao.insert(it)
                }
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }

        }.asLiveData()
    }


    fun updateProfileInfo(
        authToken: AuthToken,
        firstName:RequestBody,
        userPic: MultipartBody.Part?
    ): LiveData<DataState<ProfileViewState>> {
        return object :
            NetworkBoundResource<BlogGetProfileInfoResponse, BlogPost, ProfileViewState>(
                sessionManager.isConnectedToTheInternet(),
                true,
                true,
                false
            ) {

            // not applicable
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<BlogGetProfileInfoResponse>) {

                Log.d(TAG,"PostCreateHouse 444444 + ${response.body.phone}")

                sessionManager.setProfileInfo(response.body.first_name,
                    response.body.birth_day,
                    response.body.gender,
                    response.body.phone,
                    response.body.email)

                withContext(Dispatchers.Main) {
                    // finish with success response
                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(response.body.id.toString(), ResponseType.Dialog())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>> {
                Log.d(TAG,"PostCreateHouse 7070707070 + ${authToken}")
                return openApiMainService.updateProfileInfo(
                    "Token ${authToken.token!!}",
                    firstName,
                    userPic
                )
            }

            // not applicable
            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let {
                    //blogPostDao.insert(it)
                }
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }

        }.asLiveData()
    }


    fun myHouseList(
        authToken: AuthToken,
        page: Int
    ): LiveData<DataState<ProfileViewState>> {

        return object: NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, ProfileViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<BlogListSearchResponse>
            ) {
                Log.d("qwe","result count ${response.body.count}")
                Log.d("qwe","result response ${response.body.results}")


                val location = arrayListOf<Point>()
                val blogPostList: ArrayList<BlogPost> = ArrayList()
                for(blogPostResponse in response.body.results){
                    location.add(Point(blogPostResponse.latitude,blogPostResponse.longitude))
                    val imagePost = blogPostResponse.photos?.get(0) ?: "//////////////////////////////////////////////////////////////////////"
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
                            image = "https://akv-technopark.herokuapp.com" + imagePost.toString().substring(24,imagePost.toString().length - 1),
                            rating = blogPostResponse.rating
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ProfileViewState(
                                ProfileViewState.MyHouseFields(
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

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> {
                return openApiMainService.getListMyHouse(
                    "Token ${authToken.token!!}",
                    page
                )
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
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
















