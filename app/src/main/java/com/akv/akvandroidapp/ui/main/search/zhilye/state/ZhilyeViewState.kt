package com.akv.akvandroidapp.ui.main.search.zhilye.state

import com.akv.akvandroidapp.entity.*

class ZhilyeViewState (
    var zhilyeFields: ZhilyeFields = ZhilyeFields(),
    var deleteblogFields: FavoriteDeleteFields = FavoriteDeleteFields(),
    var createblogFields: FavoriteCreateFields = FavoriteCreateFields()

    )
{
    data class ZhilyeFields(
        var zhilyeDetail: ZhilyeDetail = ZhilyeDetail(
            0,"","",0,0,"",0.0,0.0,"",
            0,false,0,0,0.0,"",false,false, 0,0),
        var zhilyeUser: UserChatMessages = UserChatMessages(0,"","","",""),
        var zhilyeDetailAccomadations: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailRules: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailNearBuildings: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailPhotos: List<ZhilyeDetailPhotos> = ArrayList(),
        var blogListRecommendations: List<BlogPost> = ArrayList(),
        var zhilyeReviewsList: List<Review> = ArrayList(),
        var zhilyeReservationsList: List<ZhilyeReservation> = ArrayList(),
        var zhilyeBlockedDatesList: List<ZhilyeBlockedDate> = ArrayList(),
        var houseId: Int = 1
    )

    data class FavoriteDeleteFields(
        var isDeleted: Boolean = false,
        var houseId: Int = 1)

    data class FavoriteCreateFields(
        var isCreated: Boolean = false,
        var houseId: Int = 1)
}








