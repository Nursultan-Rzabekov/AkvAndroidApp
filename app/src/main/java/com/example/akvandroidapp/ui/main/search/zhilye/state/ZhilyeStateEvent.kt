package com.example.akvandroidapp.ui.main.search.zhilye.state


sealed class ZhilyeStateEvent {

    class BlogZhilyeEvent : ZhilyeStateEvent()

    class DeleteFavoriteItemEvent: ZhilyeStateEvent()

    class СreateFavoriteItemEvent: ZhilyeStateEvent()

    class None: ZhilyeStateEvent()

}