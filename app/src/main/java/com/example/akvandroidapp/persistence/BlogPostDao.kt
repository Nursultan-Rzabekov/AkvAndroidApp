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
        OR price <= :price__gte || '%' 
        OR price >= :price__lte || '%'
        ORDER BY id  ASC LIMIT (:page * :pageSize)""")
    fun searchBlogPostsOrderByDateASC(
        query: String,
        price__gte:Int,
        price__lte: Int,
        page: Int,
        pageSize: Int = PAGINATION_PAGE_SIZE
    ): LiveData<List<BlogPost>>
}






