package com.akv.akvandroidapprelease.ui.main.search.zhilye.state

import com.akv.akvandroidapprelease.entity.ReservationRequestInfo

class ZhilyeBookViewState (
    var reservationRequestField: ReservationRequestField = ReservationRequestField()
){
    data class ReservationRequestField(
        var response: ReservationRequestInfo = ReservationRequestInfo("")
    )
}







