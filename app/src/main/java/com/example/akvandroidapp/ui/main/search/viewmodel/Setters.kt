package com.example.akvandroidapp.ui.main.search.viewmodel

import android.net.Uri
import com.example.akvandroidapp.entity.*
import com.example.akvandroidapp.ui.main.favorite.viewmodel.FavoriteViewModel
import com.example.akvandroidapp.ui.main.home.viewmodel.HomeViewModel
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewModel
import com.example.akvandroidapp.ui.main.messages.viewmodel.MessagesViewModel
import com.example.akvandroidapp.ui.main.profile.state.ProfileViewState
import com.example.akvandroidapp.ui.main.profile.viewmodel.ProfileViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.state.ZhilyeViewState
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import com.example.akvandroidapp.util.DateUtils
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

fun DetailsViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.target = query
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

fun ProfileViewModel.setHouseId(houseId: Int){
    val update = getCurrentViewStateOrNew()
    update.myHouseStateFields.houseId = houseId
    setViewState(update)
}

fun ProfileViewModel.setState(state: Int){
    val update = getCurrentViewStateOrNew()
    update.myHouseStateFields.state = state
    setViewState(update)
}


fun ZhilyeBookViewModel.setResponse(response: Boolean) {
    val update = getCurrentViewStateOrNew()
    update.reservationRequestField.response
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

fun SearchViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}

fun FavoriteViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
    setViewState(update)
}


fun MessagesViewModel.setBlogListData(blogList: List<UserChatMessages>){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.blogList = blogList
    setViewState(update)
}

fun ZhilyeReviewViewModel.setBlogListData(reviewList: List<Review>){
    val update = getCurrentViewStateOrNew()
    update.reviewsField.reviewList = reviewList
    setViewState(update)
}

fun DetailsViewModel.setBlogListData(blogList: List<UserConversationMessages>){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.blogList = blogList
    setViewState(update)
}

fun DetailsViewModel.setBlogListDataImages(blogList: List<UserConversationImages?>){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.blogListImages = blogList
    setViewState(update)
}


fun ProfileViewModel.setBlogListData(blogList: List<BlogPost>){
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

fun FavoriteViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun ProfileViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myHouseFields.isQueryExhausted = isExhausted
    setViewState(update)
}

fun MessagesViewModel.setQueryExhausted(isExhausted: Boolean){
    val update = getCurrentViewStateOrNew()
    update.myChatFields.isQueryExhausted = isExhausted
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

fun SearchViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun FavoriteViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
    setViewState(update)
}

fun ProfileViewModel.setQueryInProgress(isInProgress: Boolean){
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







