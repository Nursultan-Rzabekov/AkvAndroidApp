package com.example.akvandroidapp.ui.main.search.zhilye.state

import com.example.akvandroidapp.entity.*

class ZhilyeViewState (
    var zhilyeFields: ZhilyeFields = ZhilyeFields(),
    var deleteblogFields: FavoriteDeleteFields = FavoriteDeleteFields(),
    var createblogFields: FavoriteCreateFields = FavoriteCreateFields()

    )
{
    data class ZhilyeFields(
        var zhilyeDetail: ZhilyeDetail = ZhilyeDetail(
            0,"","",0,0,"",0.0,0.0,"",
            0,false,0,0,0.0,"",false,0,0),
        var zhilyeUser: UserChatMessages = UserChatMessages(0,"","","",""),
        var zhilyeDetailAccomadations: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailRules: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailNearBuildings: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailPhotos: List<ZhilyeDetailPhotos> = ArrayList(),
        var blogListRecommendations: List<BlogPost> = ArrayList(),
        var zhilyeReviewsList: List<Review> = ArrayList(),
        var houseId: Int = 1
    )

    data class FavoriteDeleteFields(
        var isDeleted: Boolean = false,
        var houseId: Int = 1)

    data class FavoriteCreateFields(
        var isDeleted: Boolean = false,
        var houseId: Int = 1)
}








