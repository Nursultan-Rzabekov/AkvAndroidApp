package com.example.akvandroidapp.ui.main.search.state

import android.net.Uri
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogQueryUtils.Companion.BLOG_ORDER_PRICE_LEFT
import com.example.akvandroidapp.persistence.BlogQueryUtils.Companion.BLOG_ORDER_PRICE_RIGHT
import com.example.akvandroidapp.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ROOMS

class SearchViewState (

    var blogFields: BlogFields = BlogFields(),
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),
    var updatedBlogFields: UpdatedBlogFields = UpdatedBlogFields()

)
{
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var rooms_left: Int = BLOG_ORDER_ROOMS,
        var rooms_right:Int = BLOG_ORDER_ROOMS,

        var price_left: Int = BLOG_ORDER_PRICE_LEFT,
        var price_right: Int = BLOG_ORDER_PRICE_RIGHT,

        var beds_left: Int = BLOG_ORDER_PRICE_LEFT,
        var beds_right: Int = BLOG_ORDER_PRICE_LEFT


    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    )

    data class UpdatedBlogFields(
        var updatedBlogTitle: String? = null,
        var updatedBlogBody: String? = null,
        var updatedImageUri: Uri? = null
    )
}








