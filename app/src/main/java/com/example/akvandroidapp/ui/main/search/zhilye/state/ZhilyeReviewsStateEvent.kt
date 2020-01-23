package com.example.akvandroidapp.ui.main.search.zhilye.state

sealed class ZhilyeReviewsStateEvent {
    class ZhilyeReviewsEvent : ZhilyeReviewsStateEvent()
    class None: ZhilyeReviewsStateEvent()
}