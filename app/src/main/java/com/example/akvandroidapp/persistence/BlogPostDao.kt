package com.example.akvandroidapp.persistence

import androidx.room.*
import com.example.akvandroidapp.entity.BlogPost

@Dao
interface BlogPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: BlogPost): Long

    @Delete
    suspend fun delete(blogPost: BlogPost)

    @Query("SELECT * FROM blog_post")
    fun getFavoriteBlogPost(): List<BlogPost>

}






