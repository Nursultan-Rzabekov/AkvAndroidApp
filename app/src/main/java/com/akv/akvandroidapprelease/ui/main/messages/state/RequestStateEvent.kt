package com.akv.akvandroidapprelease.ui.main.messages.state




sealed class RequestStateEvent {
    object None: RequestStateEvent()
    object OrdersListStateEvent: RequestStateEvent()
    class AcceptReservationEvent(
        var reservation_id: Int = -1
    ): RequestStateEvent()
    class RejectReservationEvent(
        var reservation_id: Int = -1,
        var message: String = ""
    ): RequestStateEvent()
}