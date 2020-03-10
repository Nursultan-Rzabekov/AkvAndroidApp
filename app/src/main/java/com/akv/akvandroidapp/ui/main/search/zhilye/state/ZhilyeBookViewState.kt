package com.akv.akvandroidapp.ui.main.search.zhilye.state

import com.akv.akvandroidapp.entity.ReservationRequestInfo

class ZhilyeBookViewState (
    var reservationRequestField: ReservationRequestField = ReservationRequestField()
){
    data class ReservationRequestField(
        var response: ReservationRequestInfo = ReservationRequestInfo("")
    )
}







