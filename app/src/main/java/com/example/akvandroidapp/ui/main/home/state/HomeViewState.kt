package com.example.akvandroidapp.ui.main.home.state

import com.example.akvandroidapp.entity.HomeReservation


class HomeViewState (
    var homeReservationField: HomeReservationField = HomeReservationField()
)
{
    data class HomeReservationField(
        var reservationList: List<HomeReservation> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var count: Int = 0
    )
}








