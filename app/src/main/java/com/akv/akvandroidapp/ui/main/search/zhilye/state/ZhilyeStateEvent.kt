package com.akv.akvandroidapp.ui.main.search.zhilye.state


sealed class ZhilyeStateEvent {

    object BlogZhilyeEvent : ZhilyeStateEvent()

    object DeleteFavoriteItemEvent: ZhilyeStateEvent()

    object CreateFavoriteItemEvent: ZhilyeStateEvent()

    object None: ZhilyeStateEvent()

}