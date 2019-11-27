package com.example.akvandroidapp.persistence

import androidx.lifecycle.LiveData
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED


class BlogQueryUtils {


    companion object{
        private val TAG: String = "AppDebug"

        // values
        const val BLOG_ORDER_ASC: String = ""
        const val BLOG_ORDER_DESC: String = "-"
        const val BLOG_FILTER_USERNAME = "username"
        const val BLOG_FILTER_DATE_UPDATED = "date_updated"

        val ORDER_BY_ASC_DATE_UPDATED = BLOG_ORDER_ASC + BLOG_FILTER_DATE_UPDATED
    }
}

fun BlogPostDao.returnOrderedBlogQuery(
    query: String,
    filterAndOrder: String,
    page: Int
): LiveData<List<BlogPost>> {

    when{
        filterAndOrder.contains(ORDER_BY_ASC_DATE_UPDATED) ->{
            return searchBlogPostsOrderByDateASC(
                query = query,
                page = page)
        }

        else ->
            return searchBlogPostsOrderByDateASC(
                query = query,
                page = page
            )
    }
}


