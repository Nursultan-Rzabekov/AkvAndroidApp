package com.akv.akvandroidapprelease.ui.main.search.zhilye.state

import com.akv.akvandroidapprelease.entity.Review

class ZhilyeReviewsViewState(
    var reviewsField: ReviewField = ReviewField(),
    var isReviewCreatedField: Boolean = false,
    var isReviewUpdatedField: Boolean = false,
    var isReviewDeletedField: Boolean = false
) {

    data class ReviewField(
        var reviewList: List<Review> = ArrayList(),
        var page: Int = 1,
        var houseId: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )
}