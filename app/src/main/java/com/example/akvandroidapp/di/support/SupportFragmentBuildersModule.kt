package com.example.akvandroidapp.di.support

import com.example.akvandroidapp.ui.main.favorite.FavoriteFragment
import com.example.akvandroidapp.ui.main.home.HomeFragment
import com.example.akvandroidapp.ui.main.messages.MessagesFragment
import com.example.akvandroidapp.ui.main.profile.AboutProfileFragment
import com.example.akvandroidapp.ui.main.profile.ProfileFragment
import com.example.akvandroidapp.ui.main.profile.account.AccountUserEditProfileFragment
import com.example.akvandroidapp.ui.main.profile.account.AccountUserProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.*
import com.example.akvandroidapp.ui.main.profile.my_house.MyHouseAddsProfileFragment
import com.example.akvandroidapp.ui.main.profile.my_house.MyHouseDetailEditProfileFragment
import com.example.akvandroidapp.ui.main.profile.my_house.MyHouseDetailProfileFragment
import com.example.akvandroidapp.ui.main.profile.my_house.MyHouseMoneyProfileFragment
import com.example.akvandroidapp.ui.main.profile.settings.SettingsGeolocationProfileFragment
import com.example.akvandroidapp.ui.main.profile.settings.SettingsProfileFragment
import com.example.akvandroidapp.ui.main.profile.settings.SettingsRegionProfileFragment
import com.example.akvandroidapp.ui.main.profile.support.GuestProfileFragment
import com.example.akvandroidapp.ui.main.profile.support.HostProfileFragment
import com.example.akvandroidapp.ui.main.profile.support.SupportProfileReviewFragment
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
abstract class SupportFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeSupportProfileReviewFragment(): SupportProfileReviewFragment
}