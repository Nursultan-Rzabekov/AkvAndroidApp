package com.akv.akvandroidapp.ui.main.profile.my_house.state

import android.os.Parcelable
import com.akv.akvandroidapp.entity.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyHouseViewState (
    var myHouseFields: MyHouseFields = MyHouseFields(),
    var myHouseStateFields: MyHouseStateFields = MyHouseStateFields(),
    var zhilyeFields: MyHouseZhilyeFields = MyHouseZhilyeFields(),
    var myHouseUpdateFields: MyHouseUpdateFields = MyHouseUpdateFields()
    ) : Parcelable
{
    @Parcelize
    data class MyHouseFields(
        var blogList: List<BlogPost> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    ) : Parcelable

    @Parcelize
    data class MyHouseStateFields(
        var houseId: Int = 1,
        var state: Int = 0,
        var response: Boolean = false,
        var message: String = ""
    ) : Parcelable

    @Parcelize
    data class MyHouseUpdateFields(
        var response: Boolean = false,
        var message: String = ""
    ) : Parcelable

    @Parcelize
    data class MyHouseZhilyeFields(
        var zhilyeDetail: ZhilyeDetail = ZhilyeDetail(
            0,"","",0,0,"",0.0,0.0,"",
            0,false,0,0,0.0,"",false,false,0,0),
        var zhilyeUser: UserChatMessages = UserChatMessages(0,"","","",""),
        var zhilyeDetailAccomadations: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailRules: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailNearBuildings: List<ZhilyeDetailProperties> = ArrayList(),
        var zhilyeDetailPhotos: List<ZhilyeDetailPhotos> = ArrayList(),
        var blogListRecommendations: List<BlogPost> = ArrayList(),
        var zhilyeReviewsList: List<Review> = ArrayList(),
        var zhilyeReservationsList: List<ZhilyeReservation> = ArrayList(),
        var zhilyeBlockedDates: List<ZhilyeBlockedDate> = ArrayList(),
        var houseId: Int = 1
    ) : Parcelable
}








