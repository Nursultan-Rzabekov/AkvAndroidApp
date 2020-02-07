package com.example.akvandroidapp.ui.main.messages.state

import com.example.akvandroidapp.ui.main.profile.state.ProfileStateEvent
import okhttp3.MultipartBody


sealed class RequestStateEvent {
    class None: RequestStateEvent()
    class OrdersListStateEvent: RequestStateEvent()
    class AcceptReservationEvent(
        var reservation_id: Int = -1
    ): RequestStateEvent()
    class RejectReservationEvent(
        var reservation_id: Int = -1
    ): RequestStateEvent()
}