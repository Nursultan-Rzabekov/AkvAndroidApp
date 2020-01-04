package com.example.akvandroidapp.ui.main.search.viewmodel

import android.net.Uri
import com.example.akvandroidapp.entity.BlogPost

fun SearchViewModel.setQuery(query: String){
    val update = getCurrentViewStateOrNew()
    update.blogFields.searchQuery = query
    setViewState(update)
}

fun SearchViewModel.setBlogListData(blogList: List<BlogPost>){
    val update = getCurrentViewStateOrNew()
    update.blogFields.blogList = blogList
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

fun SearchViewModel.setQueryInProgress(isInProgress: Boolean){
    val update = getCurrentViewStateOrNew()
    update.blogFields.isQueryInProgress = isInProgress
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
        body = blogPost.description
    ) // update UpdateBlogFragment (not really necessary since navigating back)
    setBlogPost(blogPost) // update ViewBlogFragment
    updateListItem(blogPost) // update BlogFragment
}







