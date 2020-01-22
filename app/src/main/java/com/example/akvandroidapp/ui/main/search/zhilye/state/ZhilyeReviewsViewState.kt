package com.example.akvandroidapp.ui.main.search.zhilye.state

import com.example.akvandroidapp.entity.Review

class ZhilyeReviewsViewState(
    var reviewsField: ReviewField = ReviewField()
) {

    data class ReviewField(
        var reviewList: List<Review> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )

}