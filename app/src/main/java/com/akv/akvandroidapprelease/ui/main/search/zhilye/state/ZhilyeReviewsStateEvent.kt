package com.akv.akvandroidapprelease.ui.main.search.zhilye.state

sealed class ZhilyeReviewsStateEvent {
    object ZhilyeReviewsEvent : ZhilyeReviewsStateEvent()
    object None: ZhilyeReviewsStateEvent()
    class CreateReviewEvent(
        var houseId: Int,
        var stars: Int,
        var body: String
    ) : ZhilyeReviewsStateEvent()
    class UpdateReviewEvent(
        var houseId: Int,
        var reviewId: Int,
        var stars: Int,
        var body: String
    ) : ZhilyeReviewsStateEvent()
}