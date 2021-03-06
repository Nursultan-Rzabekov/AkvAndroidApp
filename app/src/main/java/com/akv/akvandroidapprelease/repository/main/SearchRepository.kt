package com.akv.akvandroidapprelease.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.akv.akvandroidapprelease.api.main.GenericResponse
import com.akv.akvandroidapprelease.api.main.OpenApiMainService
import com.akv.akvandroidapprelease.api.main.bodies.CreateReservationBody
import com.akv.akvandroidapprelease.api.main.responses.*
import com.akv.akvandroidapprelease.entity.*
import com.akv.akvandroidapprelease.persistence.BlogPostDao
import com.akv.akvandroidapprelease.repository.JobManager
import com.akv.akvandroidapprelease.repository.NetworkBoundResource
import com.akv.akvandroidapprelease.session.SessionManager
import com.akv.akvandroidapprelease.ui.DataState
import com.akv.akvandroidapprelease.ui.Response
import com.akv.akvandroidapprelease.ui.ResponseType
import com.akv.akvandroidapprelease.ui.main.search.state.SearchViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeBookViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeReviewsViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeViewState
import com.akv.akvandroidapprelease.util.*
import com.akv.akvandroidapprelease.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.yandex.mapkit.geometry.Point
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
        startDate: String,
        endDate:String,
        adultsCount:Int,
        childrenCount:Int,
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
                    Log.e("SearchRepository", "is favorite ${blogPostResponse.toString()}")
                    location.add(Point(blogPostResponse.latitude, blogPostResponse.longitude))
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
                Log.e("SearchRepository", "is favorite ${blogPostList}")

                sessionManager.locationItemCount(location)
                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = SearchViewState(SearchViewState.BlogFields(
                                blogPostList,
                                isQueryInProgress = false,
                                isQueryExhausted = booleanQuery(response.body.count)
                            ))
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostListSize: Int):Boolean{
                if(page * PAGINATION_PAGE_SIZE >= blogPostListSize){
                    return true
                }
                return false
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> {

                Log.d("qwe","result search ${query_name}")

                val data: MutableMap<String, String> = HashMap()

                if(startDate == "" || endDate == ""){
                    data["date_start"] = startDate
                    data["date_end"] = endDate
                }

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
                    authorization = "Token ${authToken.token!!}",
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
        authToken: AuthToken,
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

                Log.d("qweqwe","house_id  + ${houseId}")

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
                    is_owner = response.body.is_owner,
                    discount7days = response.body.discount7days,
                    discount30days = response.body.discount30days
                )


                val blogZhilyePhotosList: ArrayList<ZhilyeDetailPhotos> = ArrayList()
                response.body.photos?.forEach {
                    val image: String = it.image
                    blogZhilyePhotosList.add(
                        ZhilyeDetailPhotos(
                            house = it.house,
                            image = "${Constants.BASE_URL_IMAGE}$image"
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


                val reviewsList = arrayListOf<Review>()
                response.body.reviews?.forEach{ review ->
                    reviewsList.add(
                        Review(
                            id = review.id,
                            house = review.house,
                            body = review.body,
                            stars = review.stars,
                            created_at = DateUtils.convertServerStringDateToLong(
                                review.created_at
                            ),
                            user_id = review.user.id,
                            first_name = review.user.first_name,
                            last_name = review.user.last_name,
                            userpic = review.user.userpic,
                            email = review.user.email
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
                            image = "${Constants.BASE_URL_IMAGE}${blogPostResponse.photos?.first()?.image}",
                            rating = blogPostResponse.rating
                        )
                    )
                }

                val reservations = arrayListOf<ZhilyeReservation>()
                response.body.reservations?.forEach {
                    reservations.add(
                        ZhilyeReservation(
                            check_in = it.check_in,
                            check_out = it.check_out,
                            user_id = it.user.id,
                            userpic = it.user.userpic,
                            first_name = it.user.first_name,
                            last_name = it.user.last_name,
                            email = it.user.email,
                            income = it.income
                        )
                    )
                }

                val blockedDates = arrayListOf<ZhilyeBlockedDate>()
                response.body.blocked_dates?.forEach {
                    blockedDates.add(
                        ZhilyeBlockedDate(
                            check_in = it.check_in,
                            check_out = it.check_out
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
                                blogListRecommendations = blogPostList,
                                zhilyeReviewsList = reviewsList,
                                zhilyeReservationsList = reservations,
                                zhilyeDetailRules = blogZhilyeRulesList,
                                zhilyeBlockedDatesList = blockedDates)
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ZhilyeResponse>> {
                return openApiMainService.getZhilyeWithHouseId(
                    "Token ${authToken.token!!}",
                    house_id = houseId)
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
                Log.d("response reservation", "response: ${response.body.check_in}")

                val responseInfo = ReservationRequestInfo(
                    check_in = response.body.check_in
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
                    body = CreateReservationBody(
                        check_in = check_in,
                        check_out = check_out,
                        guests = guests,
                        house_id = house_id)
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

    fun getReviewsForHouse(
        house_id: Int,
        page: Int
    ): LiveData<DataState<ZhilyeReviewsViewState>>{
        return object : NetworkBoundResource<ReviewsListResponse, List<BlogPost>, ZhilyeReviewsViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ReviewsListResponse>) {
                Log.d("response reviews", "response_count: ${response.body.count}")

                val reviewsList = arrayListOf<Review>()
                for (review in response.body.results){
                    reviewsList.add(
                        Review(
                            id = review.id,
                            house = review.house,
                            body = review.body,
                            stars = review.stars,
                            created_at = DateUtils.convertServerStringDateToLong(
                                review.created_at
                            ),
                            user_id = review.user.id,
                            first_name = review.user.first_name,
                            last_name = review.user.last_name,
                            userpic = review.user.userpic,
                            email = review.user.email
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ZhilyeReviewsViewState(ZhilyeReviewsViewState.ReviewField(
                                reviewList = reviewsList,
                                isQueryInProgress = false,
                                isQueryExhausted = booleanQuery(response.body.count))
                            )
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

            override fun createCall(): LiveData<GenericApiResponse<ReviewsListResponse>> {
                Log.d("Reviews request cal", "send house_id $house_id")
                return openApiMainService.getReviewsForHouse(
                    house_id = house_id,
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeReviewsViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("reviewsList", job)
            }

        }.asLiveData()
    }

    fun updateReviewForHouse(
        authToken: AuthToken,
        houseId: Int,
        reviewId: Int,
        body: String,
        stars: Int
    ): LiveData<DataState<ZhilyeReviewsViewState>>{
        return object: NetworkBoundResource<ReviewResponse, List<BlogPost>, ZhilyeReviewsViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ReviewResponse>) {
                Log.d("Review update response", "response ${response.body.id}")

                if (response.body.house <= 0)
                    return onCompleteJob(DataState.error(
                            Response("Error", ResponseType.Dialog())
                        )
                    )

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ZhilyeReviewsViewState(
                                isReviewUpdatedField = response.body.house > 0
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ReviewResponse>> {
                Log.d("Review update call", "send house_id $houseId")
                return openApiMainService.updateReviewForHouse(
                    "Token ${authToken.token!!}",
                    house_id = houseId,
                    review_id = reviewId,
                    body = body,
                    stars = stars
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeReviewsViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
            }

            override fun setJob(job: Job) {
                addJob("reviewUpdate", job)
            }

        }.asLiveData()
    }

    fun deleteReviewForHouse(
        authToken: AuthToken,
        houseId: Int,
        reviewId: Int,
        body: String
    ): LiveData<DataState<ZhilyeReviewsViewState>>{
        return object : NetworkBoundResource<GenericResponse, List<BlogPost>, ZhilyeReviewsViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {
                Log.d("response delete resp", "response state: ${response.body.response}")

                if (!response.body.response)
                    return onCompleteJob(DataState.error(
                        Response("Error", ResponseType.Dialog())
                        )
                    )

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ZhilyeReviewsViewState(
                                isReviewDeletedField = response.body.response
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                Log.d("Reviews delete cal", "send house_id $houseId")
                return openApiMainService.deleteReviewForHouse(
                    "Token ${authToken.token!!}",
                    house_id = houseId,
                    review_id = reviewId,
                    body = body
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeReviewsViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
            }

            override fun setJob(job: Job) {
                addJob("deleteReview", job)
            }

        }.asLiveData()
    }

    fun addReviewForHouse(
        authToken: AuthToken,
        houseId: Int,
        stars: Int,
        body: String
    ): LiveData<DataState<ZhilyeReviewsViewState>>{
        return object: NetworkBoundResource<GenericResponse, List<BlogPost>, ZhilyeReviewsViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {
                Log.d("response post review", "response state: ${response.body.response}")

                if(!response.body.response){
                    return onCompleteJob(DataState.error(
                            Response("Error", ResponseType.Dialog())
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = ZhilyeReviewsViewState(
                                isReviewCreatedField = response.body.response
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                Log.d("Reviews post cal", "send house_id $houseId")
                return openApiMainService.createReviewForHouse(
                    "Token ${authToken.token!!}",
                    house_id = houseId,
                    stars = stars,
                    body = body
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeReviewsViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("reviewsList", job)
            }

        }.asLiveData()
    }

    fun deleteMyFavoritePosts(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<ZhilyeViewState>> {

        return object: NetworkBoundResource<GenericResponse, List<BlogPost>, ZhilyeViewState>(
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
                Log.e(TAG,"qweqweqweqweqwe ${response.body.response}")
                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            ZhilyeViewState(
                                deleteblogFields = ZhilyeViewState.FavoriteDeleteFields(response.body.response))
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                Log.e(TAG,"qweqweqweqweqwe ${houseId}")
                return openApiMainService.deleteFavoritePost(
                    "Token ${authToken.token!!}",
                    house_id = houseId
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeViewState> {
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


    fun createMyFavoritePosts(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<ZhilyeViewState>> {

        return object: NetworkBoundResource<GenericResponse, List<BlogPost>, ZhilyeViewState>(
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
                            data = ZhilyeViewState( createblogFields =
                            ZhilyeViewState.FavoriteCreateFields(response.body.response))
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.createFavoritePost(
                    "Token ${authToken.token!!}",
                    house_id = houseId
                )
            }

            override fun loadFromCache(): LiveData<ZhilyeViewState> {
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


    fun deleteMyFavoritePostsSearch(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<SearchViewState>> {

        return object: NetworkBoundResource<GenericResponse, List<BlogPost>, SearchViewState>(
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
                            data = SearchViewState( deleteblogFields =
                            SearchViewState.FavoriteDeleteFields(response.body.response))
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

            override fun loadFromCache(): LiveData<SearchViewState> {
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


    fun createMyFavoritePostsSearch(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<SearchViewState>> {

        return object: NetworkBoundResource<GenericResponse, List<BlogPost>, SearchViewState>(
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
                            data = SearchViewState( createblogFields =
                            SearchViewState.FavoriteCreateFields(response.body.response))
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.createFavoritePost(
                    "Token ${authToken.token!!}",
                    house_id = houseId
                )
            }

            override fun loadFromCache(): LiveData<SearchViewState> {
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
















