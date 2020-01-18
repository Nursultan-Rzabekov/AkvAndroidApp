package com.example.akvandroidapp.ui.main.search.zhilye.state

import com.example.akvandroidapp.entity.*

class ZhilyeViewState (
    var zhilyeFields: ZhilyeFields = ZhilyeFields()

    )
{
    data class ZhilyeFields(
        var zhilyeDetail: ZhilyeDetail = ZhilyeDetail(
            0,"","",0,0,"",0.0,0.0,"",
            0,0,0,0,0.0,"",false,0,0),
        var zhilyeUser: UserChatMessages = UserChatMessages(0,"","","",""),
        var zhilyeDetailAccomadations: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailRules: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailNearBuildings: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailPhotos: List<ZhilyeDetailPhotos> = ArrayList(),
        var blogListRecommendations: List<BlogPost> = ArrayList(),
        var houseId: Int = 1
    )
}








