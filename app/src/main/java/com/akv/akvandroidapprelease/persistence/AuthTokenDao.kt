package com.akv.akvandroidapprelease.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.akv.akvandroidapprelease.entity.AuthToken

@Dao
interface AuthTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authToken: AuthToken): Long

    @Query("UPDATE auth_token SET token = null WHERE id = :id")
    fun nullifyToken(id: Int): Int

    @Query("SELECT * FROM auth_token WHERE id= :id")
    suspend fun searchByPk(id: Int): AuthToken?
}


















