package com.akv.akvandroidapp.ui.main.profile.support.viewmodel



sealed class SupportStateEvent {

    object None: SupportStateEvent()
    class FeedbackSendEvent(var message: String): SupportStateEvent()
}