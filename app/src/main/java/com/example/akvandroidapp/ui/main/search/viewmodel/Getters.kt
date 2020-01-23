package com.example.akvandroidapp.ui.main.search.viewmodel

import android.net.Uri
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.ui.main.favorite.viewmodel.FavoriteViewModel
import com.example.akvandroidapp.ui.main.home.viewmodel.HomeViewModel
import com.example.akvandroidapp.ui.main.messages.detailState.DetailsViewModel
import com.example.akvandroidapp.ui.main.messages.viewmodel.MessagesViewModel
import com.example.akvandroidapp.ui.main.profile.viewmodel.ProfileViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeBookViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeViewModel
import com.example.akvandroidapp.ui.main.search.zhilye.viewmodels.ZhilyeReviewViewModel
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

fun DetailsViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.myChatFields.page
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

fun DetailsViewModel.getEmailName(): String {
    getCurrentViewStateOrNew().let {
        return it.sendMessageFields.email
    }
}


fun DetailsViewModel.getTargetQuery(): String {
    getCurrentViewStateOrNew().let {
        return it.myChatFields.target
    }
}

fun ZhilyeBookViewModel.getResponse(): Boolean{
    getCurrentViewStateOrNew().let {
        return it.reservationRequestField.response.response
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
    return BlogPost(-1, "" , 0,0, false, 0.0, 0.0, "","",0,0,"",0.0)
}

fun SearchViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}


fun ProfileViewModel.getPage(): Int{
    getCurrentViewStateOrNew().let {
        return it.myHouseFields.page
    }
}

fun ProfileViewModel.getHouseId(): Int{
    getCurrentViewStateOrNew().let {
        return it.myHouseStateFields.houseId
    }
}


// 0 -> activate
// 1 -> deactivate
fun ProfileViewModel.getState(): Int{
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












