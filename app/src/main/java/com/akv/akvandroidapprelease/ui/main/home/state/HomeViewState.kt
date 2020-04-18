package com.akv.akvandroidapprelease.ui.main.home.state

import com.akv.akvandroidapprelease.entity.HomeReservation
import com.akv.akvandroidapprelease.entity.PayReservationIdResponse


class HomeViewState (
    var homeReservationField: HomeReservationField = HomeReservationField(),
    var cancelReservationField: CancelReservationField = CancelReservationField(),
    var payReservationField: PayReservationField = PayReservationField()
    )
{
    data class HomeReservationField(
        var reservationList: List<HomeReservation> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var count: Int = 0
    )

    data class CancelReservationField(
        var isCancelled: Boolean = false,
        var message: String? = null
    )

    data class PayReservationField(
        var isPayed: Boolean = false,
        var reservationResponse : PayReservationIdResponse = PayReservationIdResponse(1,"","")
    )
}








