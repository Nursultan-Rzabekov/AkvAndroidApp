package com.akv.akvandroidapp.repository.main


import android.util.Log
import androidx.lifecycle.LiveData
import com.akv.akvandroidapp.api.auth.network_responses.CodeResponse
import com.akv.akvandroidapp.api.main.GenericResponse
import com.akv.akvandroidapp.api.main.OpenApiMainService
import com.akv.akvandroidapp.api.main.responses.*
import com.akv.akvandroidapp.entity.*
import com.akv.akvandroidapp.persistence.BlogPostDao
import com.akv.akvandroidapp.repository.JobManager
import com.akv.akvandroidapp.repository.NetworkBoundResource
import com.akv.akvandroidapp.session.SessionManager
import com.akv.akvandroidapp.ui.DataState
import com.akv.akvandroidapp.ui.Response
import com.akv.akvandroidapp.ui.ResponseType
import com.akv.akvandroidapp.ui.auth.state.VerifyCodeFields
import com.akv.akvandroidapp.ui.main.profile.add_ad.AddAdViewState
import com.akv.akvandroidapp.ui.main.profile.add_ad.DatesTemp
import com.akv.akvandroidapp.ui.main.profile.my_house.state.MyHouseViewState
import com.akv.akvandroidapp.ui.main.profile.payment.viewmodel.PaymentViewState
import com.akv.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.akv.akvandroidapp.ui.main.profile.support.viewmodel.SupportViewState
import com.akv.akvandroidapp.util.*
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
        list: ArrayList<MultipartBody.Part>?,
        photos: ArrayList<MultipartBody.Part>?,
        blockedDates: ArrayList<DatesTemp>,
        house_type_id:RequestBody,
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

                    onCompleteJob(
                        DataState.data(
                            null,
                            Response(response.body.id.toString(), ResponseType.Dialog())
                        )
                    )

                    sessionManager.setSuccess(Constants.SUCCESS)

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
                    list = list,
                    listBlockedDates = blockedDates,
                    house_type_id = house_type_id,
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
                    iban = response.body.iban,
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
                                iban = response.body.iban,
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
        iBan:RequestBody,
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

                Log.d(TAG,"PostCreateHouse 7070707070 + ${response.body.userpic}")

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
                                    iban = response.body.iban,
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
                    gender = gender,
                    iban = iBan
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
                            data = MyHouseViewState(
                                MyHouseViewState.MyHouseFields(
                                    blogPostList,
                                    isQueryInProgress = false,
                                    isQueryExhausted = booleanQuery(response.body.count)
                                )
                            )
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostListSize: Int):Boolean{
                if(page * Constants.PAGINATION_PAGE_SIZE >= blogPostListSize){
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

                Log.d(TAG, "my house activate: ${response.body.response}")

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = MyHouseViewState(
                                myHouseStateFields =
                                MyHouseViewState.MyHouseStateFields(
                                    response = response.body.response,
                                    message = response.body.message.toString())
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
                Log.d(TAG, "my house activate: ${response.body.response}")

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = MyHouseViewState(
                                myHouseStateFields =
                                MyHouseViewState.MyHouseStateFields(
                                    response = response.body.response,
                                    message = response.body.message.toString()
                                )
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
        authToken: AuthToken,
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
                Log.d("getZhilyeWithHouseId","house_status  + ${response.body.status}")

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
                            image = "${Constants.BASE_URL_IMAGE}${image}"
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
                                    zhilyeDetailRules = blogZhilyeRulesList,
                                    zhilyeBlockedDates = blockedDates
                                )
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
        nearsList: List<String>?,
        facilitiesList: List<String>?,
        rulesList: List<String>?,
        datesList: List<String>?
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
//                rulesList?.forEach {
//                    multipartBodyRulesList.add(MultipartBody.Part.createFormData("rules",it))
//                }
//                if(rulesList != null){
//                    dataList["rules"] = multipartBodyRulesList
//                }
//
//                facilitiesList?.forEach {
//                    multipartBodyFacilitiesListList.add(MultipartBody.Part.createFormData("accommodations",it))
//                }
//                if(facilitiesList != null){
//                    dataList["accommodations"] = multipartBodyFacilitiesListList
//                }
//
//                nearsList?.forEach {
//                    multipartBodyNearBuildingsList.add(MultipartBody.Part.createFormData("near_buildings",it))
//                }
//                if(nearsList != null){
//                    dataList["near_buildings"] = multipartBodyNearBuildingsList
//                }
//
//                datesList?.forEach {
//                    multipartBodyBlockedDatesList.add(MultipartBody.Part.createFormData("blocked_dates",it))
//                }
//                if(datesList != null){
//                    dataList["blocked_dates"] = multipartBodyBlockedDatesList
//                }

                val data: HashMap<String, RequestBody> = HashMap()

                if (title != null)
                    data["name"] = title
                if (description != null)
                    data["description"] = description
                if (address != null)
                    data["address"] = address
                if (price != null)
                    data["price"] = price

                val multipartBodyList: ArrayList<MultipartBody.Part> = arrayListOf()

                rulesList?.forEach {
                    multipartBodyList.add(MultipartBody.Part.createFormData("rules",it))
                }

                facilitiesList?.forEach {
                    multipartBodyList.add(MultipartBody.Part.createFormData("accommodations",it))
                }

                nearsList?.forEach {
                    multipartBodyList.add(MultipartBody.Part.createFormData("near_buildings",it))
                }

                datesList?.forEach {
                    multipartBodyList.add(MultipartBody.Part.createFormData("blocked_dates",it))
                }

                return openApiMainService.updateZhilyeInfo(
                    "Token ${authToken.token!!}",
                    house_id = houseId,
                    options = data,
                    list = multipartBodyList
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

    fun getPaymentsHistory(
        authToken: AuthToken,
        page: Int
    ): LiveData<DataState<PaymentViewState>>{
        return object: NetworkBoundResource<PaymentsListResponse, List<BlogPost>, PaymentViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true

        ){
            override suspend fun createCacheRequestAndReturn() {
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<PaymentsListResponse>) {
                Log.d("getPaymentsHistory","payment history count + ${response.body.count}")

                val paymentHisory = mutableListOf<PaymentHistoryItem>()

                response.body.results.forEach {
                    paymentHisory.add(
                        PaymentHistoryItem(
                            it.id,
                            it.amount,
                            it.is_paid,
                            it.payment_id,
                            it.reservation_id
                        )
                    )
                }

                withContext(Dispatchers.Main){
                    onCompleteJob(
                        DataState.data(
                            data = PaymentViewState(
                                    paymentHistoryField = PaymentViewState.PaymentHistoryField(
                                        payments = paymentHisory,
                                        isQueryInProgress = false,
                                        isQueryExhausted = booleanQuery(response.body.count)
                                )
                            )
                        )
                    )
                }
            }

            private fun booleanQuery(blogPostListSize: Int):Boolean{
                if(page * Constants.PAGINATION_PAGE_SIZE >= blogPostListSize){
                    return true
                }
                return false
            }

            override fun createCall(): LiveData<GenericApiResponse<PaymentsListResponse>> {
                return openApiMainService.getPayments(
                    "Token ${authToken.token!!}",
                    page = page
                )
            }

            override fun loadFromCache(): LiveData<PaymentViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {
            }

            override fun setJob(job: Job) {
                addJob("getPaymentsHistory", job)
            }

        }.asLiveData()
    }

    fun sendFeedback(
        authToken: AuthToken,
        message: RequestBody
    ): LiveData<DataState<SupportViewState>>{
        return object: NetworkBoundResource<FeedbackSendResponse, List<BlogPost>, SupportViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            false,
            true

        ){
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<FeedbackSendResponse>) {
                Log.d(TAG, "feedback sended ${response.body.id}")
                withContext(Dispatchers.Main) {
                    onCompleteJob(
                        DataState.data(
                            data = SupportViewState(
                                id = response.body.id
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<FeedbackSendResponse>> {
                Log.d(TAG, "send feedback $message")
                return openApiMainService.sendProblem(
                    "Token ${authToken.token!!}",
                    message
                )
            }

            override fun loadFromCache(): LiveData<SupportViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<BlogPost>?) {

            }

            override fun setJob(job: Job) {
                addJob("sendFeedback", job)
            }

        }.asLiveData()
    }

    fun sendCode(phone: String): LiveData<DataState<ProfileViewState>>{
        return object: NetworkBoundResource<CodeResponse, Any, ProfileViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }


            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<CodeResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response.body.response}")

                if(!response.body.response){
                    return onErrorReturn(response.body.message, true, false)
                }

                onCompleteJob(
                    DataState.data(
                        data = ProfileViewState(
                            isCodeSend = response.body.response
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<CodeResponse>> {
                return openApiMainService.
                    sendCode(phone)
            }

            override fun setJob(job: Job) {
                addJob("attemptSendCode", job)
            }
        }.asLiveData()
    }

    fun verifyCode(phone: String, code:String): LiveData<DataState<ProfileViewState>>{

        val verifyCodeFieldErrors = VerifyCodeFields(phone,code).isValidForSendCode()
        if(!verifyCodeFieldErrors.equals(VerifyCodeFields.VerifyCodeError.none())){
            return returnErrorResponse(verifyCodeFieldErrors, ResponseType.Dialog())
        }

        return object: NetworkBoundResource<GenericResponse, Any, ProfileViewState>(
            sessionManager.isConnectedToTheInternet(),
            true,
            true,
            false
        ){

            // Ignore
            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            // Ignore
            override suspend fun updateLocalDb(cacheObject: Any?) {

            }

            // not used in this case
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<GenericResponse>) {

                Log.d(TAG, "handleApiSuccessResponse: ${response.body.response}")

                if(!response.body.response){
                    return onErrorReturn(response.body.response.toString(), true, false)
                }

                onCompleteJob(
                    DataState.data(
                        data = ProfileViewState(
                            isPhoneNumberValid = response.body.response
                        )
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                return openApiMainService.verifyCode(phone,code)
            }

            override fun setJob(job: Job) {
                addJob("attemptSendCode", job)
            }
        }.asLiveData()
    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<ProfileViewState>>{
        Log.d(TAG, "returnErrorResponse: ${errorMessage}")

        return object: LiveData<DataState<ProfileViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }
}
















