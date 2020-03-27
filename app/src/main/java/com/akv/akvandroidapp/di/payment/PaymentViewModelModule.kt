package com.akv.akvandroidapp.di.payment

import androidx.lifecycle.ViewModel
import com.akv.akvandroidapp.di.ViewModelKey
import com.akv.akvandroidapp.ui.main.profile.payment.viewmodel.PaymentViewModel
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