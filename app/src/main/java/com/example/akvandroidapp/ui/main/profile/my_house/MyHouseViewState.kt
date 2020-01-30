package com.example.akvandroidapp.ui.main.profile.my_house

import android.os.Parcelable
import com.example.akvandroidapp.entity.BlogPost
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyHouseViewState (
    var myHouseFields: MyHouseFields = MyHouseFields(),
    var myHouseStateFields: MyHouseStateFields = MyHouseStateFields()
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
        var state: Int = 0
    ) : Parcelable

}








