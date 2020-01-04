package com.example.akvandroidapp.ui.main.search.viewmodel

import android.net.Uri
import com.example.akvandroidapp.entity.BlogPost


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

fun SearchViewModel.getAccomadations(): String {
    getCurrentViewStateOrNew().let {
        return it.blogFields.accomadations
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

fun SearchViewModel.getSlug(): String{
    getCurrentViewStateOrNew().let {
        it.viewBlogFields.blogPost?.let {
            return it.description!!
        }
    }
    return ""
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
    return BlogPost(-1, "" , "", 0, 0,"", 0.0, 0.0,"","",0,0,"",0.0)
}

fun SearchViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}












