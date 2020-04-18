package com.akv.akvandroidapprelease.ui.main.search.zhilye.state


sealed class ZhilyeBookStateEvent {

    object None: ZhilyeBookStateEvent()
    data class ReservationEvent(
        var check_in: String = "",
        var check_out: String = "",
        var guests: Int = -1,
        var houseId: Int = -1
    ): ZhilyeBookStateEvent()
}