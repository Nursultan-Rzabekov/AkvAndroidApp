package com.akv.akvandroidapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akv.akvandroidapp.entity.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Query("SELECT * FROM account_properties WHERE phone = :phone")
    suspend fun searchByPhone(phone: String): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE id = :id")
    fun searchByPk(id: Int): LiveData<AccountProperties>

    @Query("SELECT * FROM account_properties WHERE id = :id")
    fun searchByPkUser(id: Int): AccountProperties

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("UPDATE account_properties SET email = :email, name = :name WHERE id = :id")
    fun updateAccountProperties(id: Int, email: String, name: String)
}













