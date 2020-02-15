package com.example.akvandroidapp.ui.main.profile.payment.viewmodel



sealed class PaymentStateEvent {

    class None: PaymentStateEvent()
    class PaymentHistoryEvent: PaymentStateEvent()
}