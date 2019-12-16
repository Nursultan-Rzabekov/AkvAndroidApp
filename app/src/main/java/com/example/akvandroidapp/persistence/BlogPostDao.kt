package com.example.akvandroidapp.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.util.Constants.Companion.PAGINATION_PAGE_SIZE

@Dao
interface BlogPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: BlogPost): Long


    @Query("""
        SELECT * FROM blog_post 
        WHERE name LIKE '%' || :query || '%' 
        OR description LIKE '%' || :query || '%' 
        OR address LIKE '%' || :query || '%' 
        ORDER BY id  ASC LIMIT (:page * :pageSize)""")
    fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>
}






