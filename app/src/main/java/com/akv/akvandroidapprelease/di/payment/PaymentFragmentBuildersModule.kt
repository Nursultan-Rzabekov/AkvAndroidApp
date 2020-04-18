package com.akv.akvandroidapprelease.di.payment


import com.akv.akvandroidapprelease.ui.main.profile.payment.PaymentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PaymentFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributePaymentFragment(): PaymentFragment


}