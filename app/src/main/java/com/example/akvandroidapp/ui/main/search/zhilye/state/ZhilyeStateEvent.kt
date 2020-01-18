package com.example.akvandroidapp.ui.main.search.zhilye.state



sealed class ZhilyeStateEvent {

    class BlogZhilyeEvent : ZhilyeStateEvent()
    class None: ZhilyeStateEvent()

}