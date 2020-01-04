package com.example.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.example.akvandroidapp.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("SearchRepository")
{
    private val TAG: String = "AppDebug"

    fun searchBlogPosts(
        authToken: AuthToken,
        city__name: String,
        accomadations:String,
        verified:String,
        ordering:String,
        type_house:Int,
        price__gte:Int,
        price__lte: Int,
        room__gte:Int,
        room__lte: Int,
        beds_gte:Int,
        beds_lte: Int,
        page: Int
    ): LiveData<DataState<SearchViewState>> {

        return object: NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, SearchViewState>(
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

                val blogPostList: ArrayList<BlogPost> = ArrayList()
                for(blogPostResponse in response.body.results){
                    blogPostList.add(
                        BlogPost(
                            id = blogPostResponse.id,
                            name = blogPostResponse.name,
                            description = blogPostResponse.description,
                            rooms = blogPostResponse.rooms,
                            floor = blogPostResponse.floor,
                            address = blogPostResponse.address,
                            longitude = blogPostResponse.longitude,
                            latitude = blogPostResponse.latitude,
                            house_type = blogPostResponse.house_type,
                            city = blogPostResponse.city,
                            price = blogPostResponse.price,
                            status = blogPostResponse.status,
                            image = "https://akv-technopark.herokuapp.com" + blogPostResponse.photos[0].toString()
                                .substring(24,blogPostResponse.photos[0].toString().length - 1),
                            rating = blogPostResponse.rating
                        )
                    )

                    Log.d("String","String just do + ${blogPostResponse.photos[0].toString().substring(24,blogPostResponse.photos[0].toString().length - 1)}")
                }


                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = SearchViewState(SearchViewState.BlogFields(blogPostList,
                                isQueryInProgress = false,
                                isQueryExhausted = booleanQuery(blogPostList)))
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostList: ArrayList<BlogPost>):Boolean{
                if(page * PAGINATION_PAGE_SIZE > blogPostList.size){
                    return true
                }
                return false
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> {

                Log.d("qwe","result price left ${price__gte}")
                Log.d("qwe","result room left ${room__gte}")
                Log.d("qwe","result price right ${price__lte}")
                Log.d("qwe","result page ${page}")
//                                    "Token ${authToken.token!!}",

                val data: MutableMap<String, String> = HashMap()

                if(city__name != "нет"){
                    data["city__name"] = city__name
                }

                if(type_house != 0){
                    data["house_type__id"] = type_house.toString()
                }
                if(accomadations != ""){
                    data["accommodations"] = accomadations
                }

                if(price__gte != 0){
                    data["price__gte"] = price__gte.toString()
                }
                if(price__lte != 0){
                    data["price__lte"] = price__lte.toString()
                }

                if(room__gte != 0){
                    data["rooms__gte"] = room__gte.toString()
                }
                if(room__lte != 0){
                    Log.d("asasdsasasdasdsdaasdad",room__lte.toString())
                    data["rooms__lte"] = room__lte.toString()
                }

                if(beds_gte != 0){
                    data["beds__gte"] = beds_gte.toString()
                }
                if(beds_lte != 0){
                    data["beds__lte"] = beds_lte.toString()
                }

                if(ordering != "нет"){
                    data["ordering"] = ordering.toString()
                }

                data["verified"] = verified.toString()

                return openApiMainService.searchListBlogPosts(
                    options = data,
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<SearchViewState> {
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
















