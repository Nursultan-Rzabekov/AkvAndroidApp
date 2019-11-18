package com.example.akvandroidapp.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * NOTES:
 * 1) local 'auth_token' table has foreign key relationship to 'account_properties' table through 'account' field (PK)
 */
@Entity(
    tableName = "auth_token",
    foreignKeys = [
        ForeignKey(
            entity = AccountProperties::class,
            parentColumns = ["pk"],
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













