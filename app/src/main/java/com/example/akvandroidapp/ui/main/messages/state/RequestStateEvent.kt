package com.example.akvandroidapp.ui.main.messages.state

import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import okhttp3.MultipartBody


sealed class RequestStateEvent {
    class None: RequestStateEvent()
    class OrdersListStateEvent: RequestStateEvent()
}