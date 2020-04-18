package com.akv.akvandroidapprelease.di


import com.akv.akvandroidapprelease.di.addadhouse.AddAddFragmentBuildersModule
import com.akv.akvandroidapprelease.di.addadhouse.AddAddModule
import com.akv.akvandroidapprelease.di.addadhouse.AddAddScope
import com.akv.akvandroidapprelease.di.addadhouse.AddAddViewModelModule
import com.akv.akvandroidapprelease.di.auth.AuthFragmentBuildersModule
import com.akv.akvandroidapprelease.di.auth.AuthModule
import com.akv.akvandroidapprelease.di.auth.AuthScope
import com.akv.akvandroidapprelease.di.auth.AuthViewModelModule
import com.akv.akvandroidapprelease.di.main.MainFragmentBuildersModule
import com.akv.akvandroidapprelease.di.main.MainModule
import com.akv.akvandroidapprelease.di.main.MainScope
import com.akv.akvandroidapprelease.di.main.MainViewModelModule
import com.akv.akvandroidapprelease.di.myHouse.MyHouseFragmentBuildersModule
import com.akv.akvandroidapprelease.di.myHouse.MyHouseModule
import com.akv.akvandroidapprelease.di.myHouse.MyHouseScope
import com.akv.akvandroidapprelease.di.myHouse.MyHouseViewModelModule
import com.akv.akvandroidapprelease.di.payment.PaymentFragmentBuildersModule
import com.akv.akvandroidapprelease.di.payment.PaymentModule
import com.akv.akvandroidapprelease.di.payment.PaymentScope
import com.akv.akvandroidapprelease.di.payment.PaymentViewModelModule
import com.akv.akvandroidapprelease.di.support.SupportFragmentBuildersModule
import com.akv.akvandroidapprelease.di.support.SupportModule
import com.akv.akvandroidapprelease.di.support.SupportScope
import com.akv.akvandroidapprelease.di.support.SupportViewModelModule
import com.akv.akvandroidapprelease.ui.auth.AuthActivity
import com.akv.akvandroidapprelease.ui.main.MainActivity
import com.akv.akvandroidapprelease.ui.main.messages.chatkit.CustomLayoutMessagesActivity
import com.akv.akvandroidapprelease.ui.main.profile.add_ad.AddAdMainActivity
import com.akv.akvandroidapprelease.ui.main.profile.add_ad.MapSetPlacemarkActivity
import com.akv.akvandroidapprelease.ui.main.profile.my_house.MyHouseMainActivity
import com.akv.akvandroidapprelease.ui.main.profile.payment.PaymentMainActivity
import com.akv.akvandroidapprelease.ui.main.profile.support.SupportMainActivity
import com.akv.akvandroidapprelease.ui.main.search.MapActivity
import com.akv.akvandroidapprelease.ui.main.search.filter.FilterCityFragment
import com.akv.akvandroidapprelease.ui.main.search.filter.FilterTypeFragment
import com.akv.akvandroidapprelease.ui.main.search.filter.FilterUdopstvaFragment
import com.akv.akvandroidapprelease.ui.main.search.filter.SearchFilterFragment
import com.akv.akvandroidapprelease.ui.main.search.zhilye.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

    @AddAddScope
    @ContributesAndroidInjector(
        modules = [AddAddModule::class, AddAddFragmentBuildersModule::class, AddAddViewModelModule::class]
    )
    abstract fun contributeAddAdMainActivity(): AddAdMainActivity

    @MyHouseScope
    @ContributesAndroidInjector(
        modules = [MyHouseModule::class, MyHouseFragmentBuildersModule::class, MyHouseViewModelModule::class]
    )
    abstract fun contributeMyHouseMainActivity(): MyHouseMainActivity

    @SupportScope
    @ContributesAndroidInjector(
        modules = [SupportModule::class, SupportFragmentBuildersModule::class, SupportViewModelModule::class]
    )
    abstract fun contributeSupportMainActivity(): SupportMainActivity

    @PaymentScope
    @ContributesAndroidInjector(
        modules = [PaymentModule::class, PaymentFragmentBuildersModule::class, PaymentViewModelModule::class]
    )
    abstract fun contributePaymentMainActivity(): PaymentMainActivity

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainFragmentBuildersModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeMapActivity(): MapActivity

    @AddAddScope
    @ContributesAndroidInjector
    abstract fun contributeMapSetPlacemarkActivity(): MapSetPlacemarkActivity

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeSearchFilterFragment(): SearchFilterFragment

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeFilterTypeFragment(): FilterTypeFragment

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeFilterUdopstvaFragment(): FilterUdopstvaFragment

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeFilterCityFragment(): FilterCityFragment

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeZhilyeFragmentActivity(): ZhilyeActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeZhilyeBookActivity(): ZhilyeBookActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeZhilyeReviewActivity(): ZhilyeReviewActivity

    @MainScope
    @ContributesAndroidInjector()
    abstract fun contributeZhilyeDatesActivity(): ZhilyeDatesActivity

    @MainScope
    @ContributesAndroidInjector
    abstract fun contributeZhilyeRulesOfHouseActivity(): ZhilyeRulesOfHouseActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class,MainViewModelModule::class])
    abstract fun contributeCustomLayoutMessagesActivity(): CustomLayoutMessagesActivity


}