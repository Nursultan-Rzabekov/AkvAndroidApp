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

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name") var name: String,

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
        if (name != other.name) return false
        if (phone != other.phone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pk
        result = 31 * result + email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + phone.hashCode()
        return result
    }

}











