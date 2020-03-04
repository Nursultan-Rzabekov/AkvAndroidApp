package com.example.akvandroidapp.ui.main.search.zhilye.state

import com.example.akvandroidapp.entity.ReservationRequestInfo

class ZhilyeBookViewState (
    var reservationRequestField: ReservationRequestField = ReservationRequestField()
){
    data class ReservationRequestField(
        var response: ReservationRequestInfo = ReservationRequestInfo("")
    )
}







