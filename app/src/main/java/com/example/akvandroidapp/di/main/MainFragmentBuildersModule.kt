package com.example.akvandroidapp.di.main

import com.example.akvandroidapp.ui.main.favorite.FavoriteFragment
import com.example.akvandroidapp.ui.main.home.HomeFragment
import com.example.akvandroidapp.ui.main.messages.MessagesFragment
import com.example.akvandroidapp.ui.main.profile.AboutProfileFragment
import com.example.akvandroidapp.ui.main.profile.ProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.*
import com.example.akvandroidapp.ui.main.profile.support.GuestProfileFragment
import com.example.akvandroidapp.ui.main.profile.support.HostProfileFragment
import com.example.akvandroidapp.ui.main.search.ApartmentsFragment
import com.example.akvandroidapp.ui.main.search.filter.SearchFilterFragment
import com.example.akvandroidapp.ui.main.search.SearchFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterCityFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterTypeFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterUdopstvaFragment
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeFragment
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeReviewFragment
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeRulesOfHouseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeHomeFragmentFragment(): HomeFragment

    @ContributesAndroidInjector()
    abstract fun contributeSearchFragmentFragment(): SearchFragment

    @ContributesAndroidInjector()
    abstract fun contributeApartmentsFragment(): ApartmentsFragment

    @ContributesAndroidInjector()
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector()
    abstract fun contributeMessagesFragment(): MessagesFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeSearchFilterFragmentFragment(): SearchFilterFragment

    @ContributesAndroidInjector()
    abstract fun contributeFilterCityFragment(): FilterCityFragment

    @ContributesAndroidInjector()
    abstract fun contributeFilterTypeFragment(): FilterTypeFragment


    @ContributesAndroidInjector()
    abstract fun contributeFilterUdopstvaFragment(): FilterUdopstvaFragment

    @ContributesAndroidInjector()
    abstract fun contributeZhilyeFragment(): ZhilyeFragment

    @ContributesAndroidInjector()
    abstract fun contributeZhilyeReviewFragment(): ZhilyeReviewFragment


    @ContributesAndroidInjector()
    abstract fun contributeZhilyeRulesOfHouseFragment(): ZhilyeRulesOfHouseFragment


    @ContributesAndroidInjector()
    abstract fun contributeProfileAddAddressFragment(): ProfileAddAddressFragment


    @ContributesAndroidInjector()
    abstract fun contributeProfileProfileAddCheckFragment(): ProfileAddCheckFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileProfileAddDescriptionFragment(): ProfileAddDescriptionFragment


    @ContributesAndroidInjector()
    abstract fun contributeProfileProfileAddGalleryFragment(): ProfileAddGalleryFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddNearFragment(): ProfileAddNearFragment


    @ContributesAndroidInjector()
    abstract fun contributePProfileAddPriceFragment(): ProfileAddPriceFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddQuestsFragment(): ProfileAddQuestsFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddRequirementFragment(): ProfileAddRequirementFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddRulesFragment(): ProfileAddRulesFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddTypeFragment(): ProfileAddTypeFragment

    @ContributesAndroidInjector()
    abstract fun contributeProfileAddPostFragment(): ProfileAddPostFragment

    @ContributesAndroidInjector()
    abstract fun contributeAboutProfileFragment(): AboutProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeHostProfileFragment(): HostProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeGuestProfileFragment(): GuestProfileFragment


}