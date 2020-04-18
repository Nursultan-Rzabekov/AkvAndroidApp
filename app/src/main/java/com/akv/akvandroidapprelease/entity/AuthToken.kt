package com.akv.akvandroidapprelease.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "auth_token",
    foreignKeys = [
        ForeignKey(
            entity = AccountProperties::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = CASCADE
        )
    ]
)
data class AuthToken(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = -1,

    @ColumnInfo(name = "token")
    @SerializedName("token")
    @Expose
    var token: String?
)













