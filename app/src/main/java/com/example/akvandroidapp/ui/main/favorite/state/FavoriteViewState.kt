package com.example.akvandroidapp.ui.main.favorite.state

import com.example.akvandroidapp.entity.BlogPost


class FavoriteViewState (
    var blogFields: FavoriteFields = FavoriteFields(),
    var deleteblogFields: FavoriteDeleteFields = FavoriteDeleteFields()
    )
{
    data class FavoriteFields(
        var blogList: List<BlogPost> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )

    data class FavoriteDeleteFields(
        var isDeleted: Boolean = false,
        var houseId: Int = 1)
}








