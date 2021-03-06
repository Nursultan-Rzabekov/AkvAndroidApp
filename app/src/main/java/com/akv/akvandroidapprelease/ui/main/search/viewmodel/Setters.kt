package com.akv.akvandroidapprelease.ui.main.search.viewmodel

import android.net.Uri
import android.util.Log
import com.akv.akvandroidapprelease.entity.*
import com.akv.akvandroidapprelease.ui.auth.AuthViewModel
import com.akv.akvandroidapprelease.ui.main.favorite.viewmodel.FavoriteViewModel
import com.akv.akvandroidapprelease.ui.main.home.viewmodel.HomeViewModel
import com.akv.akvandroidapprelease.ui.main.messages.detailState.DetailsViewModel
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.MessagesViewModel
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.RequestViewModel
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseViewModel
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseViewState
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentViewModel
import com.akv.akvandroidapprelease.ui.main.profile.state.ProfileViewState
import com.akv.akvandroidapprelease.ui.main.profile.support.viewmodel.SupportViewModel
import com.akv.akvandroidapprelease.ui.main.profile.viewmodel.ProfileViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.ZhilyeViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.state.ZhilyeViewState
import com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import com.akv.akvandroidapprelease.util.DateUtils
import java.util.*

fun SearchViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.searchQuery = query
    setViewState(update)
}

fun SearchViewModel.setStartDateFilter(start: Date){
    val update = getCurrentViewStateOrNew()
    update.blogFields.dateStart = DateUtils.convertDateToString(start)
    setViewState(update)
}

fun SearchViewModel.setEndDateFilter(end: Date){
    val update = getCurrentViewStateOrNew()
    update.blogFields.dateEnd = DateUtils.convertDateToString(end)
    setViewState(update)
}

fun SearchViewModel.clearDateFilter(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.dateStart = ""
    update.blogFields.dateEnd = ""
    setViewState(update)
}

fun SearchViewModel.setAdultCount(count: Int){
    val update = getCurrentViewStateOrNew()
    update.blogFields.adultsCount = count
    setViewState(update)
}

fun SearchViewModel.setChildrenCount(count: Int){
    val update = getCurrentViewStateOrNew()
    update.blogFields.childrenCount = count
    setViewState(update)
}

fun SearchViewModel.clearCounts(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.adultsCount = 0
    update.blogFields.childrenCount = 0
    setViewState(update)
}

fun DetailsViewModel.setQuery(userId: Int){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.target = userId
    setViewState(update)
}

fun ZhilyeViewModel.setHouseId(houseId: Int){
    val update = getCurrentViewStateOrNew()
    update.zhilyeFields.houseId = houseId
    setViewState(update)
}

fun ZhilyeReviewViewModel.setHouseId(houseId: Int){
    val update = getCurrentViewStateOrNew()
    update.reviewsField.houseId = houseId
    setViewState(update)
}

fun FavoriteViewModel.setHouseId(houseId: Int){
    val update = getCurrentViewStateOrNew()
    update.deleteblogFields.houseId = houseId
    setViewState(update)
}

fun MyHouseViewModel.setHouseId(houseId: Int){
    val update = getCurrentViewStateOrNew()
    update.myHouseStateFields.houseId = houseId
    setViewState(update)
}

fun MyHouseViewModel.setZhilyeHouseId(houseId: Int){
    val update = getCurrentViewStateOrNew()
    update.zhilyeFields.houseId = houseId
    setViewState(update)
}

fun MyHouseViewModel.setState(state: Int){
    val update = getCurrentViewStateOrNew()
    update.myHouseStateFields.state = state
    setViewState(update)
}

fun MyHouseViewModel.setZhilyeData(zhilyeFields: MyHouseViewState.MyHouseZhilyeFields){
    val update = getCurrentViewStateOrNew()
    update.zhilyeFields.blogListRecommendations = zhilyeFields.blogListRecommendations
    update.zhilyeFields.zhilyeDetail = zhilyeFields.zhilyeDetail
    update.zhilyeFields.zhilyeDetailAccomadations = zhilyeFields.zhilyeDetailAccomadations
    update.zhilyeFields.zhilyeDetailNearBuildings = zhilyeFields.zhilyeDetailNearBuildings
    update.zhilyeFields.zhilyeDetailPhotos = zhilyeFields.zhilyeDetailPhotos
    update.zhilyeFields.zhilyeDetailRules = zhilyeFields.zhilyeDetailRules
    update.zhilyeFields.zhilyeUser = zhilyeFields.zhilyeUser
    update.zhilyeFields.zhilyeReviewsList = zhilyeFields.zhilyeReviewsList
    update.zhilyeFields.zhilyeReservationsList = zhilyeFields.zhilyeReservationsList
    update.zhilyeFields.zhilyeBlockedDates = zhilyeFields.zhilyeBlockedDates
    setViewState(update)
}

fun MyHouseViewModel.setUpdateResponse(updateFields: MyHouseViewState.MyHouseUpdateFields){
    val update = getCurrentViewStateOrNew()
    update.myHouseUpdateFields.response = updateFields.response
    update.myHouseUpdateFields.message = updateFields.message
    setViewState(update)
}

fun MyHouseViewModel.setResponseState(state: Boolean){
    val update = getCurrentViewStateOrNew()
    Log.e("qwe","qweqweqweqweqwe +${state}")
    update.myHouseStateFields.response = state
    setViewState(update)
}

fun ZhilyeBookViewModel.setResponse(response: String) {
    val update = getCurrentViewStateOrNew()
    update.reservationRequestField.response.check_in = response
    setViewState(update)
}

fun ZhilyeViewModel.setZhilyeData(zhilyeFields: ZhilyeViewState.ZhilyeFields){
    val update = getCurrentViewStateOrNew()
    update.zhilyeFields.blogListRecommendations = zhilyeFields.blogListRecommendations
    update.zhilyeFields.zhilyeDetail = zhilyeFields.zhilyeDetail
    update.zhilyeFields.zhilyeDetailAccomadations = zhilyeFields.zhilyeDetailAccomadations
    update.zhilyeFields.zhilyeDetailNearBuildings = zhilyeFields.zhilyeDetailNearBuildings
    update.zhilyeFields.zhilyeDetailPhotos = zhilyeFields.zhilyeDetailPhotos
    update.zhilyeFields.zhilyeDetailRules = zhilyeFields.zhilyeDetailRules
    update.zhilyeFields.zhilyeUser = zhilyeFields.zhilyeUser
    update.zhilyeFields.zhilyeReviewsList = zhilyeFields.zhilyeReviewsList
    update.zhilyeFields.zhilyeReservationsList = zhilyeFields.zhilyeReservationsList
    update.zhilyeFields.zhilyeBlockedDatesList = zhilyeFields.zhilyeBlockedDatesList
    setViewState(update)
}

fun ZhilyeViewModel.setDeleteFavourite(state: Boolean){
    val update = getCurrentViewStateOrNew()
    update.deleteblogFields.isDeleted = state
    setViewState(update)
}

fun ZhilyeViewModel.setCreateFavourite(state: Boolean){
    val update = getCurrentViewStateOrNew()
    update.createblogFields.isCreated = state
    setViewState(update)
}

fun ProfileViewModel.setProfileInfo(profileInfoFields: ProfileViewState.ProfileInfoFields){
    val update = getCurrentViewStateOrNew()
    update.profileInfoFields.email = profileInfoFields.email
    update.profileInfoFields.first_name = profileInfoFields.first_name
    update.profileInfoFields.newImageUri = profileInfoFields.newImageUri
    update.profileInfoFields.gender = profileInfoFields.gender
    update.profileInfoFields.birth_day = profileInfoFields.birth_day
    update.profileInfoFields.phone = profileInfoFields.phone
    setViewState(update)
}

fun ProfileViewModel.setProfileInfoUpdate(profileInfoUpdateFields: ProfileViewState.ProfileInfoUpdateFields){
    val update = getCurrentViewStateOrNew()
    update.profileInfoUpdateFields.email = profileInfoUpdateFields.email
    update.profileInfoUpdateFields.first_name = profileInfoUpdateFields.first_name
    update.profileInfoUpdateFields.newImageUri = profileInfoUpdateFields.newImageUri
    update.profileInfoUpdateFields.gender = profileInfoUpdateFields.gender
    update.profileInfoUpdateFields.birth_day = profileInfoUpdateFields.birth_day
    update.profileInfoUpdateFields.phone = profileInfoUpdateFields.phone
    setViewState(update)
}

fun ProfileViewModel.setProfileInfoValidation(isValid: Boolean){
    val update = getCurrentViewStateOrNew()
    update.isPhoneNumberValid = isValid
    setViewState(update)
}

fun ProfileViewModel.setProfileInfoCodeSend(isSend: Boolean){
    val update = getCurrentViewStateOrNew()
    update.isCodeSend = isSend
    setViewState(update)
}

fun SearchViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}

fun AuthViewModel.setBlogListData(blogList: AccountProperties?){
    val update = getCurrentViewStateOrNew()
    update.authViewStateResponse?.state = blogList
    setViewState(update)
}

fun PaymentViewModel.setPaymentHistoryData(payments: List<PaymentHistoryItem>){
    val update = getCurrentViewStateOrNew()
    update.paymentHistoryField.payments = payments
    setViewState(update)
}

fun FavoriteViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}

fun FavoriteViewModel.setDeleteState(delete: Boolean){
    val update = getCurrentViewStateOrNew()
    update.deleteblogFields.isDeleted = delete
    setViewState(update)
}

fun DetailsViewModel.setSendedState(sended: Boolean){
    val update = getCurrentViewStateOrNew()
    update.sendMessageFields.sended = sended
    setViewState(update)
}

fun HomeViewModel.setCancelState(cancel: Boolean){
    val update = getCurrentViewStateOrNew()
    update.cancelReservationField.isCancelled = cancel
    setViewState(update)
}

fun HomeViewModel.setPayState(pay: Boolean){
    val update = getCurrentViewStateOrNew()
    update.payReservationField.isPayed = pay
    setViewState(update)
}


fun MessagesViewModel.setBlogListData(blogList: List<UserChatMessages>){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.blogList = blogList
    setViewState(update)
}

fun RequestViewModel.setOrderListData(blogList: List<HomeReservation>){
    val update = getCurrentViewStateOrNew()
    update.ordersField.orders = blogList
    setViewState(update)
}

fun RequestViewModel.setAcceptState(state: Boolean, message: String){
    val update = getCurrentViewStateOrNew()
    update.acceptReservationField.isAccepted = state
    update.acceptReservationField.message = message
    setViewState(update)
}

fun RequestViewModel.setRejectState(state: Boolean, message: String){
    val update = getCurrentViewStateOrNew()
    update.rejectReservationField.isRejected = state
    update.rejectReservationField.message = message
    setViewState(update)
}

fun ZhilyeReviewViewModel.setBlogListData(reviewList: List<Review>){
    val update = getCurrentViewStateOrNew()
    update.reviewsField.reviewList = reviewList
    setViewState(update)
}

fun ZhilyeReviewViewModel.setIsReviewCreated(isReviewCreated: Boolean){
    val update = getCurrentViewStateOrNew()
    update.isReviewCreatedField = isReviewCreated
    setViewState(update)
}

fun ZhilyeReviewViewModel.setIsReviewUpdated(isReviewUpdated: Boolean){
    val update = getCurrentViewStateOrNew()
    update.isReviewUpdatedField = isReviewUpdated
    setViewState(update)
}

fun DetailsViewModel.setBlogListData(blogList: List<UserConversationMessages>){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.blogList = blogList
    setViewState(update)
}

fun SupportViewModel.setFeedbackId(id: Int) {
    val update = getCurrentViewStateOrNew()
    update.id = id
    setViewState(update)
}

fun MyHouseViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.myHouseFields.blogList = blogList
    setViewState(update)
}

fun HomeViewModel.setBlogListData(blogList: List<HomeReservation>){
    val update = getCurrentViewStateOrNew()
    update.homeReservationField.reservationList = blogList
    setViewState(update)
}

fun SearchViewModel.setBlogPost(blogPost: BlogPost){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.blogPost = blogPost
    setViewState(update)
}

fun SearchViewModel.setIsAuthorOfBlogPost(isAuthorOfBlogPost: Boolean){
    val update = getCurrentViewStateOrNew()
    update.viewBlogFields.isAuthorOfBlogPost = isAuthorOfBlogPost
    setViewState(update)
}

fun SearchViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun PaymentViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.paymentHistoryField.isQueryExhausted = isExhausted
    setViewState(update)
}

fun FavoriteViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun MyHouseViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myHouseFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun MessagesViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun RequestViewModel.setOrderQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.ordersField.isQueryExhausted = isExhausted
    setViewState(update)
}

fun DetailsViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun HomeViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.homeReservationField.isQueryExhausted = isExhausted
    setViewState(update)
}

fun ZhilyeReviewViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.reviewsField.isQueryExhausted = isExhausted
    setViewState(update)
}

fun AuthViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.authViewStateResponse?.isQueryExhausted = isExhausted
    setViewState(update)
}

fun SearchViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun AuthViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.authViewStateResponse?.isQueryInProgress = isInProgress
    setViewState(update)
}

fun PaymentViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.paymentHistoryField.isQueryInProgress = isInProgress
    setViewState(update)
}

fun FavoriteViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun MyHouseViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myHouseFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun ZhilyeReviewViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.reviewsField.isQueryInProgress = isInProgress
    setViewState(update)
}

fun MessagesViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun RequestViewModel.setOrderQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.ordersField.isQueryInProgress = isInProgress
    setViewState(update)
}

fun DetailsViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun HomeViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.homeReservationField.isQueryInProgress = isInProgress
    setViewState(update)
}

fun HomeViewModel.setCount(count: Int){
    val update = getCurrentViewStateOrNew()
    update.homeReservationField.count = count
    setViewState(update)
}

fun SearchViewModel.setCityQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.city_name = query
    setViewState(update)
}

fun SearchViewModel.setBlogFilterTypeHouse(type: Int){
    val update = getCurrentViewStateOrNew()
    update.blogFields.type_house = type
    setViewState(update)
}

fun SearchViewModel.setBlogFilterAccomadations(type: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.accomadations = type
    setViewState(update)
}

fun SearchViewModel.setBlogVerified(type: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.verified = type
    setViewState(update)
}

fun SearchViewModel.setBlogOrdering(type: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.ordering = type
    setViewState(update)
}

fun SearchViewModel.setBlogFilterPrice(price_left: Int, price_right: Int){
    val update = getCurrentViewStateOrNew()
    update.blogFields.price_left = price_left
    update.blogFields.price_right = price_right
    setViewState(update)
}


fun SearchViewModel.setBlogFilterRooms(rooms_left: Int, rooms_right: Int){
    val update = getCurrentViewStateOrNew()
    update.blogFields.rooms_left = rooms_left
    update.blogFields.rooms_right = rooms_right
    setViewState(update)
}


fun SearchViewModel.setBlogFilterBeds(beds_left: Int, beds_right: Int){
    val update = getCurrentViewStateOrNew()
    update.blogFields.beds_left = beds_left
    update.blogFields.beds_right = beds_right
    setViewState(update)
}

fun SearchViewModel.removeDeletedBlogPost(){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for(i in 0..(list.size - 1)){
        if(list[i] == getBlogPost()){
            list.remove(getBlogPost())
            break
        }
    }
    setBlogListData(list)
}

fun SearchViewModel.setUpdatedBlogFields(title: String?, body: String?, uri: Uri?){
    val update = getCurrentViewStateOrNew()
    val updatedBlogFields = update.updatedBlogFields
    title?.let{ updatedBlogFields.updatedBlogTitle = it }
    body?.let{ updatedBlogFields.updatedBlogBody = it }
    uri?.let{ updatedBlogFields.updatedImageUri = it }
    update.updatedBlogFields = updatedBlogFields
    setViewState(update)
}


fun SearchViewModel.updateListItem(newBlogPost: BlogPost){
    val update = getCurrentViewStateOrNew()
    val list = update.blogFields.blogList.toMutableList()
    for(i in 0..(list.size - 1)){
        if(list[i].id == newBlogPost.id){
            list[i] = newBlogPost
            break
        }
    }
    update.blogFields.blogList = list
    setViewState(update)
}


fun SearchViewModel.onBlogPostUpdateSuccess(blogPost: BlogPost){
    setUpdatedBlogFields(
        uri = null,
        title = blogPost.name,
        body = blogPost.house_type
    ) // update UpdateBlogFragment (not really necessary since navigating back)
    setBlogPost(blogPost) // update ViewBlogFragment
    updateListItem(blogPost) // update BlogFragment
}







