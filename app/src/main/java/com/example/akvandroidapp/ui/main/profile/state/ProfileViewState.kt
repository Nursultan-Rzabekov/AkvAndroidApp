package com.example.akvandroidapp.ui.main.profile.state

import android.net.Uri
import android.os.Parcelable
import com.example.akvandroidapp.entity.BlogPost
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileViewState (
    var myHouseFields: MyHouseFields = MyHouseFields(),
    var profileInfoFields: ProfileInfoFields = ProfileInfoFields(),
    var blogFields: NewBlogFields = NewBlogFields()
) : Parcelable
{
    @Parcelize
    data class NewBlogFields(
        var newBlogTitle: String? = null,
        var newBlogBody: String? = null,
        var newImageUri: Uri? = null
    ) : Parcelable

    @Parcelize
    data class ProfileInfoFields(
        var phone: String? = null,
        var birth_day: String? = null,
        var gender: Int? = null,
        var user_type: Int? = null,
        var first_name: String? = null,
        var last_name: String? = null,
        var id: Int? = null,
        var email: String? = null
//        var newImageUri: Uri? = null
    ) : Parcelable

    @Parcelize
    data class MyHouseFields(
        var blogList: List<BlogPost> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    ) : Parcelable
}








