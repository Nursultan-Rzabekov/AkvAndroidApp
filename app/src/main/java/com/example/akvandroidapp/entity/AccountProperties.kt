package com.example.akvandroidapp.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "account_properties")
data class AccountProperties(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pk") var pk: Int,

    @SerializedName("email")
    @Expose
    @ColumnInfo(name = "email") var email: String,

    @SerializedName("first_name")
    @Expose
    @ColumnInfo(name = "first_name") var first_name: String,

    @SerializedName("last_name")
    @Expose
    @ColumnInfo(name = "last_name") var last_name: String,

    @SerializedName("phone")
    @Expose
    @ColumnInfo(name = "phone") var phone: String

)
{

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as AccountProperties

        if (pk != other.pk) return false
        if (email != other.email) return false
        if (first_name != other.first_name) return false
        if (last_name != other.last_name) return false
        if (phone != other.phone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pk
        result = 31 * result + email.hashCode()
        result = 31 * result + first_name.hashCode()
        result = 31 * result + last_name.hashCode()
        result = 31 * result + phone.hashCode()
        return result
    }

}











