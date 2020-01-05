package com.example.akvandroidapp.ui.main.profile.state

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileViewState (
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








