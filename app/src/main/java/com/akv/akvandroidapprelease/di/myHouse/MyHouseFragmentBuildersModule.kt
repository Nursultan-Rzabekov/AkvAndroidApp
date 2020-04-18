package com.akv.akvandroidapprelease.di.myHouse

import com.akv.akvandroidapprelease.ui.main.profile.my_house.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyHouseFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseAddsProfileFragment(): MyHouseAddsProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseDetailProfileFragment(): MyHouseDetailProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseMoneyProfileFragment(): MyHouseMoneyProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseDetailEditProfileFragment(): MyHouseDetailEditProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseAvailableDatesEditProfileFragment(): MyHouseAvailableDatesEditProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseFacilitiesEditProfileFragment(): MyHouseFacilitiesEditProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseNearEditProfileFragment(): MyHouseNearEditProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeMyHouseRulesEditProfileFragment(): MyHouseRulesEditProfileFragment

}