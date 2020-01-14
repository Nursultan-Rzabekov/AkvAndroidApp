package com.example.akvandroidapp.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "blog_conversation")
data class UserConversationMessages(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "user")
    var user: String? = null,

    @ColumnInfo(name = "recipient")
    var recipient: String? = null,

    @ColumnInfo(name = "body")
    var body: String? = null,

    @ColumnInfo(name = "created_at")
    var created_at: String? = null,

    @ColumnInfo(name = "updated_at")
    var updated_at: String? = null

    ): Parcelable
