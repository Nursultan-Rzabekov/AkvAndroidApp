package com.akv.akvandroidapprelease.ui.main.search.state

import android.net.Uri
import com.akv.akvandroidapprelease.entity.BlogPost

class SearchViewState (
    var blogFields: BlogFields = BlogFields(),
    var viewBlogFields: ViewBlogFields = ViewBlogFields(),
    var updatedBlogFields: UpdatedBlogFields = UpdatedBlogFields(),
    var deleteblogFields: FavoriteDeleteFields = FavoriteDeleteFields(),
    var createblogFields: FavoriteCreateFields = FavoriteCreateFields()

)
{
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,

        var city_name: String = "нет",
        var type_house: Int = 0,
        var accomadations:String = "",
        var rooms_left: Int = 0,
        var rooms_right:Int = 0,

        var price_left: Int = 0,
        var price_right: Int = 0,

        var beds_left: Int = 0,
        var beds_right: Int = 0,

        var verified: String = "false",
        var ordering: String = "нет",

        var dateStart: String = "",
        var dateEnd: String = "",

        var adultsCount: Int = 0,
        var childrenCount: Int = 0

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

    data class FavoriteDeleteFields(
        var isDeleted: Boolean = false,
        var houseId: Int = 1)

    data class FavoriteCreateFields(
        var isDeleted: Boolean = false,
        var houseId: Int = 1)
}








