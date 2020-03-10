package com.akv.akvandroidapp.ui.main.messages.state


import com.akv.akvandroidapp.entity.HomeReservation


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








