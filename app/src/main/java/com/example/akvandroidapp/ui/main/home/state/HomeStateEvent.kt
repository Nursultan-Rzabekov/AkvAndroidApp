package com.example.akvandroidapp.ui.main.home.state


sealed class HomeStateEvent {
    class HomeInfoEvent: HomeStateEvent()
    class HomeCancelReservationEvent(
        var reservation_id: Int = -1,
        var message:String = ""
    ): HomeStateEvent()
    class HomePayReservationEvent(
        var reservation_id: Int = -1
    ): HomeStateEvent()
    class None: HomeStateEvent()
}