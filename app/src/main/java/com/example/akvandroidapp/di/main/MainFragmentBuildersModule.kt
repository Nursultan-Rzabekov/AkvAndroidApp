package com.example.akvandroidapp.di.main

import com.example.akvandroidapp.ui.main.favorite.FavoriteFragment
import com.example.akvandroidapp.ui.main.home.HomeFragment
import com.example.akvandroidapp.ui.main.home.PayBoxPayWebViewFragment
import com.example.akvandroidapp.ui.main.messages.ChatMesFragment
import com.example.akvandroidapp.ui.main.messages.MessagesFragment
import com.example.akvandroidapp.ui.main.messages.RequestFragment
import com.example.akvandroidapp.ui.main.profile.AboutProfileFragment
import com.example.akvandroidapp.ui.main.profile.ProfileFragment
import com.example.akvandroidapp.ui.main.profile.account.AccountUserEditProfileFragment
import com.example.akvandroidapp.ui.main.profile.account.AccountUserProfileFragment
import com.example.akvandroidapp.ui.main.profile.settings.SettingsGeolocationProfileFragment
import com.example.akvandroidapp.ui.main.profile.settings.SettingsProfileFragment
import com.example.akvandroidapp.ui.main.profile.settings.SettingsRegionProfileFragment
import com.example.akvandroidapp.ui.main.search.ApartmentsFragment
import com.example.akvandroidapp.ui.main.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeHomeFragmentFragment(): HomeFragment

    @ContributesAndroidInjector()
    abstract fun contributePayBoxPayWebViewFragment(): PayBoxPayWebViewFragment

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
    abstract fun contributeAboutProfileFragment(): AboutProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeSettingsProfileFragment(): SettingsProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeSettingsGeolocationProfileFragment(): SettingsGeolocationProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeSettingsRegionProfileFragment(): SettingsRegionProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountUserEditProfileFragment(): AccountUserEditProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeAccountUserProfileFragment(): AccountUserProfileFragment

    @ContributesAndroidInjector()
    abstract fun contributeChatMesFragment(): ChatMesFragment

    @ContributesAndroidInjector()
    abstract fun contributeRequestFragment(): RequestFragment

}