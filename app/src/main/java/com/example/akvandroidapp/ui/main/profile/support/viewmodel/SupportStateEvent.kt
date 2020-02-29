package com.example.akvandroidapp.ui.main.profile.support.viewmodel



sealed class SupportStateEvent {

    class None: SupportStateEvent()
    class FeedbackSendEvent(var message: String): SupportStateEvent()
}