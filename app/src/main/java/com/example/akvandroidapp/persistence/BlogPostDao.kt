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
        WHERE title LIKE '%' || :query || '%' 
        OR body LIKE '%' || :query || '%' 
        OR username LIKE '%' || :query || '%' 
        ORDER BY date_updated  ASC LIMIT (:page * :pageSize)""")
    fun searchBlogPostsOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>
}






