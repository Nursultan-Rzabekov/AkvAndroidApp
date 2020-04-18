package com.akv.akvandroidapprelease.di.payment

import androidx.lifecycle.ViewModel
import com.akv.akvandroidapprelease.di.ViewModelKey
import com.akv.akvandroidapprelease.ui.main.profile.payment.viewmodel.PaymentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PaymentViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel::class)
    abstract fun bindPaymentViewModel(authViewModel: PaymentViewModel): ViewModel

}