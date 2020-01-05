package com.example.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.BlogCreateUpdateResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Response
import com.example.akvandroidapp.ui.ResponseType
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.DateUtils
import com.example.akvandroidapp.util.GenericApiResponse
import com.example.akvandroidapp.util.SuccessHandling.Companion.RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER
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
        name: String,
        description: String,
        rooms:Int,
        address:String,
        longitude:Double,
        latitude:Double,
        city_id:Int,
        price:Int,
        beds:Int,
        guests:Int,
        rules:String,
        near_buildings:String,
        blocked_dates:String,
        photos: ArrayList<MultipartBody.Part>?,
        house_type_id:Int,
        accommodations:String
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

                Log.d(TAG,"PostCreateHouse 444444 + ${response.body}")

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
                            Response(response.body.name, ResponseType.Dialog())
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogCreateUpdateResponse>> {
                Log.d(TAG,"PostCreateHouse 3333333 + ${name}")
                return openApiMainService.createBlog(
                    "Token ${authToken.token!!}",
                    name,
                    description,
                    rooms,
                    4,
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


}
















