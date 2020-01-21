package com.example.akvandroidapp.repository.main


import com.yandex.mapkit.geometry.Point
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.BlogListSearchResponse
import com.example.akvandroidapp.api.main.responses.ReservationRequestResponse
import com.example.akvandroidapp.api.main.responses.ZhilyeResponse
import com.example.akvandroidapp.entity.*
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.main.search.state.SearchViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.util.AbsentLiveData
import com.example.akvandroidapp.util.ApiSuccessResponse
import com.example.akvandroidapp.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.example.akvandroidapp.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import javax.inject.Inject
import kotlin.math.roundToInt

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
        query_name:String,
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

                val location = arrayListOf<Point>()
                val blogPostList: ArrayList<BlogPost> = ArrayList()
                for(blogPostResponse in response.body.results){
                    location.add(Point(blogPostResponse.latitude,blogPostResponse.longitude))
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
                            image = "https://akv-technopark.herokuapp.com${blogPostResponse.photos?.first()?.image}",
                            rating = blogPostResponse.rating
                        )
                    )
                }

                sessionManager.locationItemCount(location)
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

                Log.d("qwe","result search ${query_name}")

                val data: MutableMap<String, String> = HashMap()

                if(query_name.isNotEmpty()){
                    data["search"] = query_name
                }

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
                    data["rooms__lte"] = room__lte.toString()
                }

                if(beds_gte != 0){
                    data["beds__gte"] = beds_gte.toString()
                }
                if(beds_lte != 0){
                    data["beds__lte"] = beds_lte.toString()
                }

                if(ordering != "нет"){
                    data["ordering"] = ordering
                }

                data["verified"] = verified

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


    fun getZhilyeWithHouseId(
        houseId: Int
    ): LiveData<DataState<ZhilyeViewState>> {

        return object: NetworkBoundResource<ZhilyeResponse, List<BlogPost>, ZhilyeViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<ZhilyeResponse>
            ) {


                val zhilyeDetail = ZhilyeDetail(
                    id = response.body.id,
                    name = response.body.name,
                    description = response.body.description,
                    rooms = response.body.rooms,
                    floor = response.body.floor,
                    address = response.body.address,
                    longitude = response.body.longitude,
                    latitude = response.body.latitude,
                    house_type = response.body.house_type,
                    price = response.body.price,
                    status = response.body.status,
                    beds = response.body.beds,
                    guests = response.body.guests,
                    rating = response.body.rating,
                    city = response.body.city,
                    is_favourite = response.body.is_favourite,
                    discount7days = response.body.discount7days,
                    discount30days = response.body.discount30days
                )


                val blogZhilyePhotosList: ArrayList<ZhilyeDetailPhotos> = ArrayList()
                response.body.photos?.forEach {
                    val image: String = it.image
                    blogZhilyePhotosList.add(
                        ZhilyeDetailPhotos(
                            house = it.house,
                            image = "https://akv-technopark.herokuapp.com$image"
                        )
                    )
                }

                val blogZhilyeAccommodationsList: ArrayList<ZhilyeDetailProperties> = ArrayList()
                response.body.accommodations?.forEach {
                    blogZhilyeAccommodationsList.add(
                        ZhilyeDetailProperties(
                            id = it.id,
                            name = it.name
                        )
                    )
                }


                val blogZhilyeRulesList: ArrayList<ZhilyeDetailProperties> = ArrayList()
                response.body.rules?.forEach {
                    blogZhilyeRulesList.add(
                        ZhilyeDetailProperties(
                            id = it.id,
                            name = it.name
                        )
                    )
                }

                val blogZhilyeNearBuildingsList: ArrayList<ZhilyeDetailProperties> = ArrayList()
                response.body.near_buildings?.forEach {
                    blogZhilyeNearBuildingsList.add(
                        ZhilyeDetailProperties(
                            id = it.id,
                            name = it.name
                        )
                    )
                }

                val userChatMessages = UserChatMessages(
                    id = response.body.user.id,
                    email = response.body.user.email,
                    first_name = response.body.user.first_name,
                    last_name = response.body.user.last_name,
                    userpic = response.body.user.userpic
                )


                val blogPostList: ArrayList<BlogPost> = ArrayList()
                for(blogPostResponse in response.body.recommendations){
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
                            image = "https://akv-technopark.herokuapp.com${blogPostResponse.photos?.first()?.image}",
                            rating = blogPostResponse.rating
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ZhilyeViewState(ZhilyeViewState.ZhilyeFields(
                                zhilyeDetail = zhilyeDetail,
                                zhilyeDetailAccomadations = blogZhilyeAccommodationsList,
                                zhilyeDetailNearBuildings = blogZhilyeNearBuildingsList,
                                zhilyeDetailPhotos = blogZhilyePhotosList,
                                zhilyeUser = userChatMessages,
                                blogListRecommendations = blogPostList))
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ZhilyeResponse>> {
                return openApiMainService.getZhilyeWithHouseId(house_id = houseId)
            }

            override fun loadFromCache(): LiveData<ZhilyeViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }

            override fun setJob(job: Job) {
                addJob("searchBlogPosts2", job)
            }

        }.asLiveData()
    }

    fun sendReservationRequest(
        authToken: AuthToken,
        check_in: String,
        check_out: String,
        guests: Int,
        house_id: Int
    ): LiveData<DataState<ZhilyeBookViewState>> {
        return object : NetworkBoundResource<ReservationRequestResponse, List<BlogPost>, ZhilyeBookViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ReservationRequestResponse>) {
                Log.d("response reservation", "response: ${response.body.response}")

                val responseInfo = ReservationRequestInfo(
                    response = response.body.response
                )

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ZhilyeBookViewState(
                                reservationRequestField = ZhilyeBookViewState.ReservationRequestField(
                                    response = responseInfo
                                )
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ReservationRequestResponse>> {
                Log.d("Reservation request cal", "send house_id $house_id")
                return openApiMainService.sendReservationRequest(
                    "Token ${authToken.token!!}",
                    check_in = check_in,
                    check_out = check_out,
                    guests = guests,
                    house_id = house_id
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeBookViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("requestReservation", job)
            }

        }.asLiveData()
    }
}
















