package com.example.akvandroidapp.ui.main.search.zhilye.state


sealed class ZhilyeStateEvent {

    class BlogZhilyeEvent : ZhilyeStateEvent()

    class DeleteFavoriteItemEvent: ZhilyeStateEvent()

    class CreateFavoriteItemEvent: ZhilyeStateEvent()

    class None: ZhilyeStateEvent()

}