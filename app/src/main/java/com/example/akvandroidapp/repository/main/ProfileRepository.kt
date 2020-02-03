package com.example.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.akvandroidapp.api.main.OpenApiMainService
import com.example.akvandroidapp.api.main.responses.*
import com.example.akvandroidapp.entity.*
import com.example.akvandroidapp.persistence.BlogPostDao
import com.example.akvandroidapp.repository.JobManager
import com.example.akvandroidapp.repository.NetworkBoundResource
import com.example.akvandroidapp.session.SessionManager
import com.example.akvandroidapp.ui.DataState
import com.example.akvandroidapp.ui.Response
import com.example.akvandroidapp.ui.ResponseType
import com.example.akvandroidapp.ui.main.profile.add_ad.AddAdViewState
import com.example.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewState
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.util.*
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
        discount30days:RequestBody,
        regionId:RequestBody,
        countryId:RequestBody
    ): LiveData<DataState<AddAdViewState>> {
        return object :
            NetworkBoundResource<BlogCreateUpdateResponse, BlogPost, AddAdViewState>(
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
                    "Token ${authToken.token!!}",
                    name = name,
                    description = description,
                    rooms = rooms,
                    floor = RequestBody.create(MediaType.parse("text/plain"), 4.toString()),
                    address = address,
                    longitude = longitude,
                    latitude = latitude,
                    city_id = city_id,
                    price = price,
                    beds = beds,
                    guests = guests,
                    rules = rules,
                    near_buildings = near_buildings,
                    house_type_id = house_type_id,
                    blocked_dates = blocked_dates,
                    accommodations = accommodations,
                    discount7days = discount7days,
                    discount30days = discount30days,
                    country_id = countryId,
                    region_id = regionId,
                    photos = photos
                )
            }

            // not applicable
            override fun loadFromCache(): LiveData<AddAdViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
                cacheObject?.let {
                    //blogPostDao.insert(it)
                }
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost3", job)
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

                sessionManager.setProfileInfo(
                    nickname = response.body.first_name,
                    birthdate = response.body.birth_day,
                    gender = response.body.gender,
                    phonenumber = response.body.phone,
                    email = response.body.email,
                    imageBackend = response.body.userpic)

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = ProfileViewState(profileInfoFields =
                            ProfileViewState.ProfileInfoFields(
                                email = response.body.email,
                                first_name = response.body.first_name,
                                newImageUri = response.body.userpic,
                                gender = response.body.gender,
                                birth_day = response.body.birth_day,
                                phone = response.body.phone
                            ))
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>> {
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
                addJob("createNewBlogPost1", job)
            }

        }.asLiveData()
    }

    fun updateProfileInfo(
        authToken: AuthToken,
        phone:RequestBody,
        email:RequestBody,
        gender:RequestBody,
        birth_day:RequestBody,
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

                Log.d(TAG,"PostCreateHouse 7070707070 + ${response.body}")

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = ProfileViewState(
                                profileInfoUpdateFields =
                                ProfileViewState.ProfileInfoUpdateFields(
                                    email = response.body.email,
                                    first_name = response.body.first_name,
                                    newImageUri = response.body.userpic,
                                    gender = response.body.gender,
                                    birth_day = response.body.birth_day,
                                    phone = response.body.phone
                                )
                            )
                        )
                    )
                }
            }
            override fun createCall(): LiveData<GenericApiResponse<BlogGetProfileInfoResponse>> {
                return openApiMainService.updateProfileInfo(
                    "Token ${authToken.token!!}",
                    phone = phone,
                    email = email,
                    birth_day = birth_day,
                    userpic = userPic,
                    gender = gender
                )
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: BlogPost?) {
            }

            override fun setJob(job: Job) {
                addJob("createNewBlogPost", job)
            }

        }.asLiveData()
    }

    fun myHouseList(
        authToken: AuthToken,
        page: Int
    ): LiveData<DataState<MyHouseViewState>> {

        return object: NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, MyHouseViewState>(
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
                            data = MyHouseViewState(
                                MyHouseViewState.MyHouseFields(
                                    blogPostList,
                                    isQueryInProgress = false,
                                    isQueryExhausted = booleanQuery(blogPostList)
                                )
                            )
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

            override fun loadFromCache(): LiveData<MyHouseViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
                // ignore
            }

            override fun setJob(job: Job) {
                addJob("myhouseBlogPosts", job)
            }

        }.asLiveData()
    }

    fun myHouseStateActivate(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<MyHouseViewState>> {

        return object: NetworkBoundResource<MyHouseStateResponse, List<BlogPost>, MyHouseViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {}

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<MyHouseStateResponse>
            ) {
                Log.d("qwe","result details ${response.body.response}")
                Log.d("qwe","result details ${response.body.message}")

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = MyHouseViewState(
                                myHouseStateFields =
                                MyHouseViewState.MyHouseStateFields()
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<MyHouseStateResponse>> {
                return openApiMainService.myHouseActivate(
                    "Token ${authToken.token!!}", houseId)
            }

            override fun loadFromCache(): LiveData<MyHouseViewState> { return AbsentLiveData.create() }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {}

            override fun setJob(job: Job) { addJob("myhouseBlogPosts", job) }

        }.asLiveData()
    }

    fun myHouseStateDeactivate(
        authToken: AuthToken,
        houseId: Int
    ): LiveData<DataState<MyHouseViewState>> {

        return object: NetworkBoundResource<MyHouseStateResponse, List<BlogPost>, MyHouseViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ) {
            // if network is down, view cache only and return
            override suspend fun createCacheRequestAndReturn() {}

            override suspend fun handleApiSuccessResponse(
                response: ApiSuccessResponse<MyHouseStateResponse>
            ) {
                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = MyHouseViewState(
                                myHouseStateFields =
                                MyHouseViewState.MyHouseStateFields()
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<MyHouseStateResponse>> {
                return openApiMainService.myHouseDeactivate(
                    "Token ${authToken.token!!}", houseId)
            }

            override fun loadFromCache(): LiveData<MyHouseViewState> { return AbsentLiveData.create() }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {}

            override fun setJob(job: Job) { addJob("myhouseBlogPosts", job) }

        }.asLiveData()
    }

    fun getZhilyeWithHouseId(
        houseId: Int
    ): LiveData<DataState<MyHouseViewState>> {

        return object: NetworkBoundResource<ZhilyeResponse, List<BlogPost>, MyHouseViewState>(
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
                            image = "https://akv-technopark.herokuapp.com${blogPostResponse.photos?.first()?.image}",
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

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = MyHouseViewState(
                                zhilyeFields = MyHouseViewState.MyHouseZhilyeFields(
                                    zhilyeDetail = zhilyeDetail,
                                    zhilyeDetailAccomadations = blogZhilyeAccommodationsList,
                                    zhilyeDetailNearBuildings = blogZhilyeNearBuildingsList,
                                    zhilyeDetailPhotos = blogZhilyePhotosList,
                                    zhilyeUser = userChatMessages,
                                    blogListRecommendations = blogPostList,
                                    zhilyeReviewsList = reviewsList,
                                    zhilyeReservationsList = reservations,
                                    zhilyeDetailRules = blogZhilyeRulesList
                                )
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ZhilyeResponse>> {
                return openApiMainService.getZhilyeWithHouseId(house_id = houseId)
            }

            override fun loadFromCache(): LiveData<MyHouseViewState> {
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

    fun updateZhilyeInfo(
        authToken: AuthToken,
        houseId: Int,
        title: RequestBody?,
        description: RequestBody?,
        address: RequestBody?,
        price: RequestBody?,
        photoList: List<RequestBody>?,
        nearsList: List<RequestBody>?,
        facilitiesList: List<RequestBody>?,
        rulesList: List<RequestBody>?,
        datesList: List<RequestBody>?
    ): LiveData<DataState<MyHouseViewState>>{
        return object: NetworkBoundResource<VerifyUpdateResponse, List<BlogPost>, MyHouseViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<VerifyUpdateResponse>) {
                Log.d(TAG,"updateZhilyeInfo + ${response.body}")

                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = MyHouseViewState(
                                myHouseUpdateFields = MyHouseViewState.MyHouseUpdateFields(
                                    response = response.body.response,
                                    message = response.body.message
                                )
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<VerifyUpdateResponse>> {

                val data: HashMap<String, RequestBody> = HashMap()
                val lists: HashMap<String, List<RequestBody>> = HashMap()

                if (title != null)
                    data["name"] = title
                if (description != null)
                    data["description"] = description
                if (address != null)
                    data["address"] = address
                if (price != null)
                    data["price"] = price

                return openApiMainService.updateZhilyeInfo(
                    "Token ${authToken.token!!}",
                    house_id = houseId,
                    options = data,
                    fac = arrayListOf("Фен")
                )
            }

            override fun loadFromCache(): LiveData<MyHouseViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
            }

            override fun setJob(job: Job) {
                addJob("updateHouseInfo", job)
            }

        }.asLiveData()
    }
}
















