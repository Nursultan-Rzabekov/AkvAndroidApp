package com.example.akvandroidapp.ui.main.search.zhilye.state

sealed class ZhilyeReviewsStateEvent {
    object ZhilyeReviewsEvent : ZhilyeReviewsStateEvent()
    object None: ZhilyeReviewsStateEvent()
}