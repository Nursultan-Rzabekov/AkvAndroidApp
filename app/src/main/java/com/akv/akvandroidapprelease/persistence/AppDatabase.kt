package com.akv.akvandroidapprelease.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akv.akvandroidapprelease.entity.AccountProperties
import com.akv.akvandroidapprelease.entity.AuthToken
import com.akv.akvandroidapprelease.entity.BlogPost

@Database(entities = [AuthToken::class, AccountProperties::class, BlogPost::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    abstract fun getBlogPostDao(): BlogPostDao

    companion object{
        val DATABASE_NAME: String = "akv_room_database_v7"
    }
}








