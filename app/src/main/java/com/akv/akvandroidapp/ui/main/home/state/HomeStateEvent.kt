package com.akv.akvandroidapp.ui.main.home.state


sealed class HomeStateEvent {
    object HomeInfoEvent: HomeStateEvent()
    class HomeCancelReservationEvent(
        var reservation_id: Int = -1,
        var message:String = ""
    ): HomeStateEvent()
    class HomePayReservationEvent(
        var reservation_id: Int = -1
    ): HomeStateEvent()
    object None: HomeStateEvent()
}