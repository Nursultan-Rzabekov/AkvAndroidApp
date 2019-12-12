package com.example.akvandroidapp.ui.main.search.viewmodel

import android.net.Uri
import com.example.akvandroidapp.entity.BlogPost


fun SearchViewModel.getFilter(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.floor
    }
}

fun SearchViewModel.getOrder(): Int {
    getCurrentViewStateOrNew().let {
        return it.blogFields.rooms
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
    return BlogPost(-1, "" , "", 1, 1,"", 1.0, 1.0,1,1,1,1.0)
}

fun SearchViewModel.getUpdatedBlogUri(): Uri? {
    getCurrentViewStateOrNew().let {
        it.updatedBlogFields.updatedImageUri?.let {
            return it
        }
    }
    return null
}











