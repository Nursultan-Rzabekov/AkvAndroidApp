package com.akv.akvandroidapp.di.payment


import com.akv.akvandroidapp.ui.main.profile.payment.PaymentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PaymentFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributePaymentFragment(): PaymentFragment


}