package com.akv.akvandroidapp.entity

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
    @ColumnInfo(name = "id") var id: Int? = null,

    @SerializedName("email")
    @Expose
    @ColumnInfo(name = "email") var email: String? = null,

    @SerializedName("gender")
    @Expose
    @ColumnInfo(name = "gender") var gender: Int? = null,

    @SerializedName("phone")
    @Expose
    @ColumnInfo(name = "phone") var phone: String? = null,

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name") var name: String? = null,

    @SerializedName("birth_day")
    @Expose
    @ColumnInfo(name = "birth_day") var birth_day: String? = null

)
{
    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as AccountProperties

        if (id != other.id) return false
        if (email != other.email) return false
        if (gender != other.gender) return false
        if (phone != other.phone) return false
        if (name != other.name) return false
        if (birth_day != other.birth_day) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (gender ?: 0)
        result = 31 * result + (phone?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (birth_day?.hashCode() ?: 0)
        return result
    }
}











