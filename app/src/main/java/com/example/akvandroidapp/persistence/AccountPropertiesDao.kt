package com.example.akvandroidapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.akvandroidapp.entity.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Query("SELECT * FROM account_properties WHERE email = :email")
    suspend fun searchByEmail(email: String): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    fun searchByPk(pk: Int): LiveData<AccountProperties>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("UPDATE account_properties SET email = :email, first_name = :first_name WHERE pk = :pk")
    fun updateAccountProperties(pk: Int, email: String, first_name: String)
}













