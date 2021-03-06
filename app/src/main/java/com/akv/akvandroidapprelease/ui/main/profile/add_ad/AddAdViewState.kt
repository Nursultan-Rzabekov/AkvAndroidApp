package com.akv.akvandroidapprelease.ui.main.profile.add_ad

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddAdViewState (
    var blogFields: NewBlogFields = NewBlogFields()
    ) : Parcelable
{
    @Parcelize
    data class NewBlogFields(
        var newBlogTitle: String? = null,
        var newBlogBody: String? = null,
        var newImageUri: Uri? = null
    ) : Parcelable

}








