package com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel

import android.os.Parcelable
import com.akv.akvandroidapprelease.entity.PaymentHistoryItem
import kotlinx.android.parcel.Parcelize

@Parcelize
class PaymentViewState (
    var paymentHistoryField: PaymentHistoryField = PaymentHistoryField()
) : Parcelable
{

    @Parcelize
    data class PaymentHistoryField(
        var payments: List<PaymentHistoryItem> = ArrayList(),
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false
    ) : Parcelable

}








