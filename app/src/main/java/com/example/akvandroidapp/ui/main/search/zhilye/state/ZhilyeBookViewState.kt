package com.example.akvandroidapp.ui.main.search.zhilye.state

import android.net.Uri
import com.example.akvandroidapp.entity.BlogPost
import com.example.akvandroidapp.entity.ReservationRequestInfo
import com.example.akvandroidapp.persistence.BlogQueryUtils.Companion.BLOG_ORDER_PRICE_LEFT
import com.example.akvandroidapp.persistence.BlogQueryUtils.Companion.BLOG_ORDER_PRICE_RIGHT

class ZhilyeBookViewState (
    var reservationRequestField: ReservationRequestField = ReservationRequestField()
){
    data class ReservationRequestField(
        var response: ReservationRequestInfo = ReservationRequestInfo("")
    )
}







