package com.akv.akvandroidapp.di.support

import com.akv.akvandroidapp.ui.main.profile.support.SupportProfileFragment
import com.akv.akvandroidapp.ui.main.profile.support.SupportProfileReviewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SupportFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeSupportProfileReviewFragment(): SupportProfileReviewFragment

    @ContributesAndroidInjector()
    abstract fun contributeSupportProfileFragment(): SupportProfileFragment

}