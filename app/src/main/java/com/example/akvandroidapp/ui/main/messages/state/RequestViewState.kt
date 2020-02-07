package com.example.akvandroidapp.ui.main.messages.state


import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.entity.UserChatMessages
import com.example.akvandroidapp.ui.main.home.state.HomeViewState


class RequestViewState (
    var ordersField: OrdersField = OrdersField(),
    var acceptReservationField: AcceptReservationField = AcceptReservationField(),
    var rejectReservationField: RejectReservationField = RejectReservationField()
)
{
    data class OrdersField(
        var orders: List<HomeReservation> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )

    data class AcceptReservationField(
        var isAccepted: Boolean = false,
        var message: String? = null
    )

    data class RejectReservationField(
        var isRejected: Boolean = false,
        var message: String? = null
    )
}








