package com.akv.akvandroidapprelease.ui.main.search.viewmodel

import android.net.Uri
import com.akv.akvandroidapprelease.entity.BlogPost
import com.akv.akvandroidapprelease.entity.UserConversationMessages
import com.akv.akvandroidapprelease.ui.main.favorite.viewmodel.FavoriteViewModel
import com.akv.akvandroidapprelease.ui.main.home.viewmodel.HomeViewModel
import com.akv.akvandroidapprelease.ui.main.messages.detailState.DetailsViewModel
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.MessagesViewModel
import com.akv.akvandroidapprelease.ui.main.messages.viewmodel.RequestViewModel
import com.akv.akvandroidapprelease.ui.main.profile.my_house.state.MyHouseViewModel
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.ZhilyeViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.akv.akvandroidapprelease.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
import okhttp3.MultipartBody


fun SearchViewModel.getFilterPriceLeft(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.price_left
    }
}

fun SearchViewModel.getFilterPriceRight(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.price_right
    }
}



fun SearchViewModel.getFilterRoomsLeft(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.rooms_left
    }
}

fun SearchViewModel.getFilterRoomsRight(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.rooms_right
    }
}

fun SearchViewModel.getFilterBedsLeft(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.beds_left
    }
}

fun SearchViewModel.getFilterBedsRight(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.beds_right
    }
}


fun SearchViewModel.getCityQuery(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.city_name
    }
}

fun SearchViewModel.getStartDateFilter(): String{
    getCurrentViewStateOrNew().let {
        return it.blogFields.dateStart
    }
}

fun SearchViewModel.getEndDateFilter(): String{
    getCurrentViewStateOrNew().let {
        return it.blogFields.dateEnd
    }
}

fun SearchViewModel.getFilterAdultsCount(): Int{
    getCurrentViewStateOrNew().let {
        return it.blogFields.adultsCount
    }
}

fun SearchViewModel.getFilterChildrenCount(): Int{
    getCurrentViewStateOrNew().let {
        return it.blogFields.childrenCount
    }
}


fun ZhilyeViewModel.getHouseId(): Int {
    getCurrentViewStateOrNew().let {
        return it.zhilyeFields.houseId
    }
}

fun ZhilyeReviewViewModel.getHouseId(): Int {
    getCurrentViewStateOrNew().let {
        return it.reviewsField.houseId
    }
}

fun FavoriteViewModel.getHouseId(): Int {
    getCurrentViewStateOrNew().let {
        return it.deleteblogFields.houseId
    }
}

fun SearchViewModel.getAccomadations(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.accomadations
    }
}

fun SearchViewModel.getOrdering(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.ordering
    }
}

fun SearchViewModel.getVerified(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.verified
    }
}

fun SearchViewModel.getTypeHouse(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.type_house
    }
}

fun SearchViewModel.getSearchQuery(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.searchQuery
    }
}

fun SearchViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.blogFields.page
    }
}

fun PaymentViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.paymentHistoryField.page
    }
}

fun ZhilyeReviewViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.reviewsField.page
    }
}

fun FavoriteViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.blogFields.page
    }
}


fun MessagesViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.myChatFields.page
    }
}

fun RequestViewModel.getOrdersPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.ordersField.page
    }
}

fun DetailsViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.myChatFields.page
    }
}

fun DetailsViewModel.getBlogList(): List<UserConversationMessages>{
    getCurrentViewStateOrNew().let {
        return it.myChatFields.blogList
    }
}

fun DetailsViewModel.getMessageBody(): String {
    getCurrentViewStateOrNew().let {
        return it.sendMessageFields.messageBody
    }
}


fun DetailsViewModel.getMessageImages(): MultipartBody.Part? {
    getCurrentViewStateOrNew().let {
        return it.sendMessageFields.images
    }
}

fun DetailsViewModel.getEmailName(): Int {
    getCurrentViewStateOrNew().let {
        return it.sendMessageFields.userId
    }
}


fun DetailsViewModel.getTargetQuery(): Int {
    getCurrentViewStateOrNew().let {
        return it.myChatFields.target
    }
}

fun ZhilyeBookViewModel.getResponse(): String{
    getCurrentViewStateOrNew().let {
        return it.reservationRequestField.response.check_in
    }
}

fun SearchViewModel.isAuthorOfBlogPost(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.isAuthorOfBlogPost
    }
}


fun SearchViewModel.getBlogPost(): BlogPost {
    getCurrentViewStateOrNew().let {
        return it.viewBlogFields.blogPost?.let {
            return it
        }?: getDummyBlogPost()
    }
}

fun SearchViewModel.getDummyBlogPost(): BlogPost {
    return BlogPost(-1, "" , 0,0, false, 0.0, 0.0, "","",0,false,"",0.0)
}

fun SearchViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}


fun MyHouseViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.myHouseFields.page
    }
}

fun MyHouseViewModel.getHouseId(): Int{
    getCurrentViewStateOrNew().let {
        return it.myHouseStateFields.houseId
    }
}

fun MyHouseViewModel.getZhilyeHouseId(): Int{
    getCurrentViewStateOrNew().let {
        return it.zhilyeFields.houseId
    }
}

// 0 -> activate
// 1 -> deactivate
fun MyHouseViewModel.getState(): Int{
    getCurrentViewStateOrNew().let {
        return it.myHouseStateFields.state
    }
}

fun HomeViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.homeReservationField.page
    }
}

fun HomeViewModel.getCount(): Int{
    getCurrentViewStateOrNew().let {
        return it.homeReservationField.count
    }
}












