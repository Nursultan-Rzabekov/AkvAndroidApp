package com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel



sealed class PaymentStateEvent {

    object None: PaymentStateEvent()
    object PaymentHistoryEvent: PaymentStateEvent()
}