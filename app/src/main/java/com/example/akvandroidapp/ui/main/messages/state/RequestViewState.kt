package com.example.akvandroidapp.ui.main.messages.state


import com.example.akvandroidapp.entity.HomeReservation
import com.example.akvandroidapp.entity.UserChatMessages


class RequestViewState (
    var ordersField: OrdersField = OrdersField()
    )
{
    data class OrdersField(
        var orders: List<HomeReservation> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    )
}








